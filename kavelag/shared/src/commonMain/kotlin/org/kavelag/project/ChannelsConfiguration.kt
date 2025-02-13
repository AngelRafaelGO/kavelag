package org.kavelag.project

import kotlinx.coroutines.channels.Channel

object SetUserConfigurationChannel {
    val incomingHttpDataChannel = Channel<HttpIncomingData>(Channel.UNLIMITED)
    val destinationServerResponseDataChannel = Channel<HttpDestinationServerResponse>(Channel.UNLIMITED)
    val proxyGenericInfoChannel = Channel<ProxyGenericInfo>(Channel.UNLIMITED)
}