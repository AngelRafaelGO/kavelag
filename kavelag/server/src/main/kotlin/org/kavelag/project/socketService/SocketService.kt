package org.kavelag.project.socketService

import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.kavelag.project.HttpDestinationServerResponse
import org.kavelag.project.HttpIncomingData
import org.kavelag.project.ProxyGenericInfo
import org.kavelag.project.SetUserConfigurationChannel
import org.kavelag.project.models.NetworkIssueErrorResponses
import org.kavelag.project.models.ProxySocketConfiguration
import org.kavelag.project.network.networkIssueSelectorOnConnect
import org.kavelag.project.network.networkIssueSelectorOnRead
import org.kavelag.project.parser.parseIncomingHttpRequest
import org.kavelag.project.targetServerProcessing.callTargetServer
import org.kavelag.project.targetServerProcessing.isPortOpen
import org.kavelag.project.targetServerProcessing.isValidUrl

suspend fun handleIncomingRequest(
    proxySocketConfiguration: ProxySocketConfiguration,
//    port: Int,
    socket: Socket
) {
    coroutineScope {
//        if (isPortOpen(proxySocketConfiguration.url, port) && isValidUrl(proxySocketConfiguration.url)) {
        try {
            val outputChannel = socket.openWriteChannel(autoFlush = true)
            val incomingHttpRequest = processIncomingRequestFromSocket(socket)

            proxySocketConfiguration.port.forEach { port ->
                if (isPortOpen(proxySocketConfiguration.url, port) && isValidUrl(proxySocketConfiguration.url)) {
                    launch {
                        SetUserConfigurationChannel.incomingHttpDataChannel.send(
                            HttpIncomingData(
                                incomingHttpRequest
                            )
                        )
                    }

                    val parsedRequest = parseIncomingHttpRequest(incomingHttpRequest)
                    val isNetworkIssueApplied =
                        networkIssueSelectorOnConnect(proxySocketConfiguration.appliedNetworkAction)

                    if (isNetworkIssueApplied) {
                        val targetServerResponse = callTargetServer(
                            proxySocketConfiguration.url,
                            port,
                            parsedRequest
                        )

                        networkIssueSelectorOnRead(proxySocketConfiguration.appliedNetworkAction)

                        if (targetServerResponse != null) {
                            // TODO: apply connect stage latency
                            launch {
                                SetUserConfigurationChannel.destinationServerResponseDataChannel.send(
                                    HttpDestinationServerResponse(
                                        "Port $port -> $targetServerResponse"
                                    )
                                )
                            }

                            outputChannel.writeStringUtf8(targetServerResponse)
                            outputChannel.flush()
                        } else {
                            launch {
                                SetUserConfigurationChannel.proxyGenericInfoChannel.send(
                                    ProxyGenericInfo(NetworkIssueErrorResponses.DESTINATION_SERVER_DID_NOT_RESPOND.message)
                                )
                            }
                        }
                    } else {
                        launch {
                            SetUserConfigurationChannel.proxyGenericInfoChannel.send(
                                ProxyGenericInfo(NetworkIssueErrorResponses.UNREACHABLE_DESTINATION_SERVER.message)
                            )
                        }
                    }
                } else {
                    launch {
                        SetUserConfigurationChannel.proxyGenericInfoChannel.send(
                            ProxyGenericInfo("Port $port -> ${NetworkIssueErrorResponses.UNAVAILABLE_PORT.message} or ${NetworkIssueErrorResponses.INVALIDE_URL.message}")
                        )
                    }
                }
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