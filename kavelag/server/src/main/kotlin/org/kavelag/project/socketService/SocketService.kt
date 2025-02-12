package org.kavelag.project.socketService

import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.kavelag.project.HttpDestinationServerResponse
import org.kavelag.project.HttpIncomingData
import org.kavelag.project.ProxyGenericInfo
import org.kavelag.project.SetUserConfigurationChannel
import org.kavelag.project.models.NetworkException
import org.kavelag.project.models.ProxySocketConfiguration
import org.kavelag.project.network.networkIssueSelector
import org.kavelag.project.parser.parseIncomingHttpRequest
import org.kavelag.project.targetServerProcessing.callTargetServer
import org.kavelag.project.targetServerProcessing.isPortOpen

suspend fun handleIncomingRequest(
    proxySocketConfiguration: ProxySocketConfiguration,
    port: Int,
    socket: Socket
) {
    coroutineScope {
        if (isPortOpen(proxySocketConfiguration.url, port)) {
            try {
                val outputChannel = socket.openWriteChannel(autoFlush = true)
                val incomingHttpRequest = processIncomingRequestFromSocket(socket)

                launch {
                    SetUserConfigurationChannel.incomingHttpDataChannel.send(HttpIncomingData(incomingHttpRequest))
                }

                val parsedRequest = parseIncomingHttpRequest(incomingHttpRequest)
                val isNetworkIssueApplied = networkIssueSelector(proxySocketConfiguration.appliedNetworkAction)

                if (isNetworkIssueApplied) {
                    val response = callTargetServer(
                        proxySocketConfiguration.url,
                        port,
                        parsedRequest
                    )

                    if (response != null) {
                        // TODO: apply connect stage latency
                        launch {
                            SetUserConfigurationChannel.destinationServerResponseDataChannel.send(
                                HttpDestinationServerResponse(
                                    "Port $port -> $response"
                                )
                            )
                        }

                        // TODO: refactor to true server response
                        val serverResponse = buildString {
                            append("HTTP/1.1 200 OK\r\n")
                            append("Content-Type: text/plain\r\n")
                            append("Content-Length: ${response.toByteArray().size}\r\n")
                            append("Connection: close\r\n")
                            append("\r\n")
                            append(response)
                        }
                        outputChannel.writeStringUtf8(serverResponse)
                        outputChannel.flush()
                        outputChannel.flushAndClose()
                        socket.close()
                    } else {
                        launch {
                            SetUserConfigurationChannel.proxyGenericInfoChannel.send(ProxyGenericInfo("The destination server did not respond"))
                        }
                    }
                } else {
                    launch {
                        SetUserConfigurationChannel.proxyGenericInfoChannel.send(ProxyGenericInfo("No action was applied to network -> simulating unreachable destination server"))
                    }
                }
            } catch (e: NetworkException) {
                println("Network issue occurred: ${e.message}")
                launch {
                    SetUserConfigurationChannel.destinationServerResponseDataChannel.send(
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
                SetUserConfigurationChannel.proxyGenericInfoChannel.send(ProxyGenericInfo("Port $port -> this port is closed or unavailable"))
            }
        }
    }
}

private suspend fun processIncomingRequestFromSocket(socket: Socket): String {
    val inputChannel = socket.openReadChannel()
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

    val incomingHttpRequest =
        requestBuilder.toString() + (if (requestBody.isNotEmpty()) "\r\n\r\n" else "\n\n") + requestBody

    return incomingHttpRequest
}