package org.kavelag.project.socketController

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

object SocketProxyReceiver {
    // TODO: move this to a shared configuration
    private const val kavelagProxyPort = 9002
    private val selectorManager = ActorSelectorManager(Dispatchers.IO)

    suspend fun launchProxySocket(messageToSend: String) {
        val serverSocket = aSocket(selectorManager).tcp().bind(port = kavelagProxyPort)
        println("Kavelag is listening on port $kavelagProxyPort")

        try {
            coroutineScope {
                while (true) {
                    val socket = serverSocket.accept()
                    println("Accepted connection: $socket")

                    launch(Dispatchers.IO) {
                        val read = socket.openReadChannel()
                        val write = socket.openWriteChannel(autoFlush = true)
                        try {
                            while (true) {
                                val line = read.readUTF8Line() ?: break
                                println("Received message: $line")
                                write.writeStringUtf8("$line\n")
                            }
                        } catch (e: Throwable) {
                            println("Error handling client: ${e.message}")
                        } finally {
                            println("Closing client socket: $socket")
                            socket.close()
                        }
                    }
                }
            }
        } catch (e: Throwable) {
            println("Server error: ${e.message}")
        } finally {
            println("Shutting down server socket and selector manager")
            serverSocket.close()
            selectorManager.close()
        }
    }
}