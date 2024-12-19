package org.kavelag.project

import kotlinx.coroutines.channels.Channel

object SetUserConfigurationChannel {
    val incomingHttpData = Channel<HttpIncomingData>(capacity = 1)
    val destinationServerResponseData = Channel<HttpDestinationServerResponse>(capacity = 1)
}