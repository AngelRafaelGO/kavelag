package com.example.socketClient

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.server.application.*
import io.ktor.utils.io.*
import java.io.InputStream
import java.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun Application.configureSocketClient() {
}

object SocketClient {
    private val selectorManager = ActorSelectorManager(Dispatchers.IO)
    private const val socketServerPort = 9002

        fun launchClient() {
            runBlocking {
                val socket = aSocket(selectorManager).tcp().connect("127.0.0.1", port = socketServerPort)
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
                yield(nextLine())
            }
        }
}
