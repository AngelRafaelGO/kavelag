package org.kavelag.project

import kotlinx.coroutines.channels.Channel

object SetUserConfigurationChannel {
    val destinationServerAddress = Channel<DestinationServerConfig>(capacity = 1)
    val appliedNetworkAction = Channel<AppliedNetworkAction>(capacity = 1)
    val incomingHttpData = Channel<HttpIncomingData>(capacity = 1)
    val destinationServerResponseData = Channel<HttpDestinationServerResponse>(capacity = 1)
}