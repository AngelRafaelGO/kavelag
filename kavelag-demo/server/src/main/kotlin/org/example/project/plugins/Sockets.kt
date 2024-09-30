package com.example.plugins

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.network.tls.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import io.ktor.websocket.*
import io.netty.util.internal.StringUtil
import java.io.InputStream
import java.time.Duration
import java.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Thread.sleep
import kotlin.system.exitProcess


fun Application.configureSockets() {
    val client = HttpClient(CIO) {
        expectSuccess = true
    }

    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    routing {
        webSocket("/ws") { // websocketSession
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val text = frame.readText()
                    val response: HttpResponse = client.get("http://localhost:8081")
                    println(response.status)

                    outgoing.send(Frame.Text("YOU SAID: $text - other server response : ${response.status}"))
                    if (text.equals("bye", ignoreCase = true)) {
                        close(CloseReason(CloseReason.Codes.NORMAL, "Client said BYE"))
                    }
                }
            }
        }
    }
}

/**
 * Two mains are provided, you must first start EchoApp.Server, and then EchoApp.Client.
 * You can also start EchoApp.Server and then use a telnet client to connect to the echo server.
 */
object EchoApp {
    //    val selectorManager = ActorSelectorManager(Dispatchers.IO)
    val DefaultPort = 42000

    object Server {
        val client = HttpClient(CIO) {
            expectSuccess = true
        }

        @JvmStatic
        fun main(args: Array<String>) {
            runBlocking {
                val selectorManager = SelectorManager(Dispatchers.IO)
                val serverSocket = aSocket(selectorManager).tcp().bind("127.0.0.1", DefaultPort)
                println("Server is listening at ${serverSocket.localAddress}")
                while (true) {
                    val socket = serverSocket.accept()
                    println("Accepted $socket")
                    launch {
                        val receiveChannel = socket.openReadChannel()
                        val sendChannel = socket.openWriteChannel(autoFlush = true)
                        var i = 0
                        try {
                            while (true) {
//                                i++
//                                println(i)
                                val someWords = receiveChannel.readUTF8Line()
                                when (someWords) {
                                    null -> {
                                        sendChannel.writeStringUtf8("Empty sentence\n")
                                        socket.close()
//                                        selectorManager.close()
//                                        exitProcess(0)
                                    }

                                    "exit" -> {
                                        sendChannel.writeStringUtf8("Server is closing\n")
                                        socket.close()
//                                        selectorManager.close()
//                                        exitProcess(0)
                                    }

                                    "hello" -> {
                                        val response: HttpResponse = client.get("http://localhost:8081/hello")
                                        sendChannel.writeStringUtf8("server answered : well received : ${response.bodyAsText()}\n")
                                    }

                                    "latency" -> {
                                        sleep(5000)
                                        val response: HttpResponse = client.get("http://localhost:8081/latency")
                                        sendChannel.writeStringUtf8("server answered : well received : ${response.bodyAsText()}\n")
                                    }

                                    "fail" -> {
                                        i++
                                        println(i)
                                        if (i % 2 != 0) {
                                            val response: HttpResponse = client.get("http://localhost:8081/fail")
                                            sendChannel.writeStringUtf8("server answered : well received : ${response.bodyAsText()}\n")
                                        } else {
                                            sendChannel.writeStringUtf8("server answered : well received : echec\n")
                                        }

                                    }

                                    else -> {
                                        val response: HttpResponse = client.get("http://localhost:8081/default")
                                        sendChannel.writeStringUtf8("server answered : well received : ${response.bodyAsText()}\n")
                                    }
                                }
                            }
                        } catch (e: Throwable) {
                            socket.close()
                        }
                    }
                }
            }
        }


    }

    object Client {
        @JvmStatic
        fun main(args: Array<String>) {
            runBlocking {
                val selectorManager = SelectorManager(Dispatchers.IO)
                val socket = aSocket(selectorManager).tcp().connect("127.0.0.1", 42000, )

                val receiveChannel = socket.openReadChannel()
                val sendChannel = socket.openWriteChannel(autoFlush = true)


                launch(Dispatchers.IO) {
                    while (true) {
                        println("enter a sentence : ")
                        val someWord = readln()
                        sendChannel.writeStringUtf8("$someWord\n")
                        val response = receiveChannel.readUTF8Line()
                        if (response != null && response != "Empty sentence" && response != "Server is closing") {
                            println(response)
                        } else {
                            println("Server closed a connection")
                            socket.close()
                            selectorManager.close()
                            exitProcess(0)
                        }
                    }
                }
            }
        }
    }
}

object TlsRawSocket {
    @JvmStatic
    fun main(args: Array<String>) {
        runBlocking {
            val selectorManager = ActorSelectorManager(Dispatchers.IO)
            val socket = aSocket(selectorManager).tcp().connect("www.google.com", port = 443)
                .tls(coroutineContext = coroutineContext)
            val write = socket.openWriteChannel()
            val EOL = "\r\n"
            write.writeStringUtf8("GET / HTTP/1.1${EOL}Host: www.google.com${EOL}Connection: close${EOL}${EOL}")
            write.flush()
            println(socket.openReadChannel().readRemaining().readBytes().toString(Charsets.UTF_8))
        }
    }
}



