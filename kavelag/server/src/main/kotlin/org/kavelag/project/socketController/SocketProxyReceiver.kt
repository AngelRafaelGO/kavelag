package org.kavelag.project.socketController

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kavelag.project.parser.parserDispatcher

object SocketProxyReceiver {
    // TODO: move this to a shared configuration
    // TODO: handle exceptions
    private const val kavelagProxyPort = 9002
    private val selectorManager = ActorSelectorManager(Dispatchers.IO)

    suspend fun launchProxySocket(messageToSend: String) {
        val serverSocket = aSocket(selectorManager).tcp().bind(port = kavelagProxyPort)
        println("Kavelag is listening on port $kavelagProxyPort")

        try {
            coroutineScope {
                while (true) {
                    val socket = serverSocket.accept()
                    launch(Dispatchers.IO) {
                        try {
                            parserDispatcher(socket.openReadChannel().readRemaining().readText())
                        } catch (e: Throwable) {
                            println("Error handling client: ${e.message}")
                        } finally {
                            try {
                                socket.close()
                            } catch (closeException: Throwable) {
                                println("Error closing socket: ${closeException.message}")
                            }
                        }
                    }
                }
            }
        } catch (e: Throwable) {
            println("Server error: ${e.message}")
        } finally {
            println("Shutting down server socket and selector manager")
            withContext(Dispatchers.IO) {
                serverSocket.close()
            }
            selectorManager.close()
        }
    }
}