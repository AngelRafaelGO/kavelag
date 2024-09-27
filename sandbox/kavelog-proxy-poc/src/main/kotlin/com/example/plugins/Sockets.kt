package com.example.plugins

import io.ktor.client.*
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
import java.io.InputStream
import java.time.Duration
import java.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


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
    val selectorManager = ActorSelectorManager(Dispatchers.IO)
    val DefaultPort = 9002

    object Server {
//        val client = HttpClient(CIO) {
//            expectSuccess = true
//        }

        @JvmStatic
        fun main(args: Array<String>) {
            runBlocking {
                val serverSocket = aSocket(selectorManager).tcp().bind(port = DefaultPort)
                println("Echo Server listening at ${serverSocket.localAddress}")
                while (true) {
                    val socket = serverSocket.accept()
                    println("Accepted $socket")
                    launch {
                        val read = socket.openReadChannel()
                        val write = socket.openWriteChannel(autoFlush = true)
                        try {
                            while (true) {
                                val line = read.readUTF8Line()
                                write.writeStringUtf8("$line\n")
//                                val response: HttpResponse = client.get("http://localhost:8080")
//                                println(response.status)
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
                val socket = aSocket(selectorManager).tcp().connect("127.0.0.1", port = DefaultPort)
                val read = socket.openReadChannel()
                val write = socket.openWriteChannel(autoFlush = true)

                launch(Dispatchers.IO) {
                    while (true) {
                        val line = read.readUTF8Line()
                        println("server: $line")
                    }
                }

                for (line in System.`in`.lines()) {
                    println("client: $line")
                    write.writeStringUtf8("$line\n")
                }
            }
        }

        private fun InputStream.lines() = Scanner(this).lines()

        private fun Scanner.lines() = sequence {
            while (hasNext()) {
                yield(readLine())
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
