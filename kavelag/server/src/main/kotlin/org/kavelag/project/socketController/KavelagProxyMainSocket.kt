package org.kavelag.project.socketController

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.kavelag.project.KAVELAG_PROXY_PORT
import org.kavelag.project.models.ProxySocketConfiguration
import org.kavelag.project.parser.parseIncomingHttpRequest
import org.kavelag.project.targetServerProcessing.callTargetServer
import java.nio.channels.ClosedSelectorException

object KavelagProxyMainSocket {
    private var selectorManager: ActorSelectorManager? = null
    private var serverSocket: ServerSocket? = null

    @Volatile
    private var isStopping = false

    suspend fun launchProxySocket(proxySocketConfiguration: ProxySocketConfiguration) {
        isStopping = false
        selectorManager = ActorSelectorManager(Dispatchers.IO)
        serverSocket = aSocket(selectorManager!!).tcp().bind(port = KAVELAG_PROXY_PORT)
        try {
            println("Socket started on port $KAVELAG_PROXY_PORT")
            coroutineScope {
                while (!isStopping) {
                    try {
                        val socket = serverSocket?.takeIf { !it.isClosed }?.accept()
                        if (socket != null) {
                            launch(Dispatchers.IO) {
                                try {
                                    val parsedRequest =
                                        parseIncomingHttpRequest(socket.openReadChannel().readRemaining().readText())
//                                    networkIssueSelector(proxySocketConfiguration.appliedNetworkAction)
                                    callTargetServer(
                                        proxySocketConfiguration.url,
                                        proxySocketConfiguration.port,
                                        parsedRequest
                                    )
                                    // TODO: handle destination server response
                                    // TODO: forward response to client
                                } catch (e: Throwable) {
                                    println("Error handling socket: $e")
                                } finally {
                                    try {
                                        socket.close()
                                    } catch (closeException: Throwable) {
                                        println("Error closing socket: $closeException")
                                    }
                                }
                            }
                        }
                    } catch (e: ClosedSelectorException) {
                        println("Selector manager was closed while accepting connections: $e")
                        break
                    } catch (e: Throwable) {
                        println("Unexpected socket error: $e")
                    }
                }
            }
        } finally {
            println("Shutting down socket...")
            cleanupProxySocketResources()
        }
    }

    suspend fun stopProxySocket() {
        if (isStopping) {
            println("Proxy socket is already stopping.")
            return
        }
        println("Stopping proxy socket...")
        isStopping = true
        try {
            serverSocket?.close()
            delay(500)
        } catch (e: Exception) {
            println("Error closing proxy socket: $e")
        }
        try {
            selectorManager?.close()
        } catch (e: Exception) {
            println("Error closing proxy socket selector manager: $e")
        }
        println("Proxy socket stopped.")
    }

    private fun cleanupProxySocketResources() {
        try {
            serverSocket?.close()
        } catch (e: Exception) {
            println("Error during proxy socket cleanup: $e")
        }
        try {
            selectorManager?.close()
        } catch (e: Exception) {
            println("Error during proxy socket selector manager cleanup: $e")
        }
        serverSocket = null
        selectorManager = null
    }
}
