package org.kavelag.project.listener

import org.kavelag.project.AppliedNetworkAction
import org.kavelag.project.SetUserConfigurationChannel

suspend fun listenerForParams(): AppliedNetworkAction? {

    for (data in SetUserConfigurationChannel.appliedNetworkAction){
        return AppliedNetworkAction(data.appliedNetworkAction, data.params)
    }
    return null
}