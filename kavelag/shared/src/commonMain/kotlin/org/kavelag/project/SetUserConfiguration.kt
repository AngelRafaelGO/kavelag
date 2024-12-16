package org.kavelag.project

import kotlinx.coroutines.channels.Channel

object SetUserConfigurationChannel {
    val configurationChannel = Channel<UserProxyConfig>(capacity = 1)
}