package org.kavelag.project.socketController

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.kavelag.project.HttpDestinationServerResponse
import org.kavelag.project.HttpIncomingData
import org.kavelag.project.KAVELAG_PROXY_PORT
import org.kavelag.project.ProxyGenericInfo
import org.kavelag.project.SetUserConfigurationChannel.destinationServerResponseDataChannel
import org.kavelag.project.SetUserConfigurationChannel.incomingHttpDataChannel
import org.kavelag.project.SetUserConfigurationChannel.proxyGenericInfoChannel
import org.kavelag.project.models.NetworkException
import org.kavelag.project.models.ProxySocketConfiguration
import org.kavelag.project.network.networkIssueSelector
import org.kavelag.project.parser.parseIncomingHttpRequest
import org.kavelag.project.targetServerProcessing.callTargetServer
import org.kavelag.project.targetServerProcessing.isPortOpen
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
                                proxySocketConfiguration.port.forEach { port ->
                                    if (isPortOpen(proxySocketConfiguration.url, port)) {
                                        try {
                                            val inputChannel = socket.openReadChannel()
                                            val outputChannel = socket.openWriteChannel(autoFlush = true)
                                            val requestBuilder = StringBuilder()
                                            var line: String?
                                            var contentLength = 0
                                            var requestBody = ""

                                            while (true) {
                                                line = inputChannel.readUTF8Line()
                                                if (line.isNullOrEmpty()) break
                                                requestBuilder.appendLine(line)
                                                // Check for "Content-Length" in case of POST requests
                                                if (line.startsWith("Content-Length:", ignoreCase = true)) {
                                                    contentLength = line.split(":")[1].trim().toInt()
                                                }
                                            }

                                            if (contentLength > 0) {
                                                requestBody = inputChannel.readPacket(contentLength).readText()
                                            }

                                            val incomingHttpRequest = requestBuilder.toString() + "\n\n" + requestBody

                                            launch {
                                                incomingHttpDataChannel.send(HttpIncomingData(incomingHttpRequest))
                                            }

                                            val parsedRequest = parseIncomingHttpRequest(incomingHttpRequest)
                                            val isNetworkIssueApplied =
                                                networkIssueSelector(proxySocketConfiguration.appliedNetworkAction)

                                            if (isNetworkIssueApplied) {
                                                val response = callTargetServer(
                                                    proxySocketConfiguration.url,
                                                    port,
                                                    parsedRequest
                                                )
                                                if (response != null) {
                                                    // TODO: apply connect stage latency
                                                    launch {
                                                        destinationServerResponseDataChannel.send(
                                                            HttpDestinationServerResponse(
                                                                "Port $port -> $response"
                                                            )
                                                        )
                                                    }
                                                    val fullResponse = buildString {
                                                        append("HTTP/1.1 200 OK\r\n")
                                                        append("Content-Type: text/plain\r\n")
                                                        append("Content-Length: ${response.toByteArray().size}\r\n")
                                                        append("Connection: close\r\n")
                                                        append("\r\n")
                                                        append(response)
                                                    }
                                                    outputChannel.writeStringUtf8(fullResponse)
                                                    outputChannel.flush()
                                                    outputChannel.flushAndClose()
                                                    socket.close()
                                                }
                                            }
                                        } catch (e: NetworkException) {
                                            println("Network issue occurred: ${e.message}")
                                            launch {
                                                destinationServerResponseDataChannel.send(
                                                    HttpDestinationServerResponse(
                                                        e.message!!
                                                    )
                                                )
                                            }
                                        } catch (e: Throwable) {
                                            println("Error handling socket: $e")
                                        } finally {
                                            try {
                                                socket.close()
                                            } catch (closeException: Throwable) {
                                                println("Error closing socket: $closeException")
                                            }
                                        }
                                    } else {
                                        launch {
                                            proxyGenericInfoChannel.send(ProxyGenericInfo("Port $port -> this port is closed or unavailable"))
                                        }
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
