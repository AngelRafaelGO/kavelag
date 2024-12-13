package org.kavelag.project.socketController

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kavelag.project.parser.parseIncomingHttpRequest

object ProxySocketReceiver {
    // TODO: move this to a shared configuration
    private const val kavelagProxyPort = 9002
    private val selectorManager = ActorSelectorManager(Dispatchers.IO)

    suspend fun launchProxySocket(messageToSend: String) {
        val serverSocket = aSocket(selectorManager).tcp().bind(port = kavelagProxyPort)
        try {
            coroutineScope {
                while (true) {
                    val socket = serverSocket.accept()
                    launch(Dispatchers.IO) {
                        try {
                            parseIncomingHttpRequest(socket.openReadChannel().readRemaining().readText())
                            // TODO: apply action to network connection
                            // TODO: send request to destination server
                            // TODO: handle destination server response
                            // TODO: forward response to client
                        } catch (e: Throwable) {
                            println("Error handling request parsing: ${e.message}")
                        } finally {
                            try {
                                socket.close()
                            } catch (closeException: Throwable) {
                                println("Error closing proxy socket: ${closeException.message}")
                            }
                        }
                    }
                }
            }
        } catch (e: Throwable) {
            println("Proxy socket error: ${e.message}")
        } finally {
            println("Shutting down proxy server socket and selector manager")
            withContext(Dispatchers.IO) {
                serverSocket.close()
            }
            selectorManager.close()
        }
    }
}