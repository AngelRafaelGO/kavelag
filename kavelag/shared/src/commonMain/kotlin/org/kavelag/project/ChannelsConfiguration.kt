package org.kavelag.project

import kotlinx.coroutines.channels.Channel

object SetUserConfigurationChannel {
    val incomingHttpData = Channel<HttpIncomingData>(Channel.UNLIMITED)
    val destinationServerResponseData = Channel<HttpDestinationServerResponse>(Channel.UNLIMITED)
}