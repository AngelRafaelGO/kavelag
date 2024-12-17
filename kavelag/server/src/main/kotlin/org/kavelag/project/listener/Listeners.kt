package org.kavelag.project.listener

import org.kavelag.project.AppliedNetworkAction
import org.kavelag.project.SetUserConfigurationChannel

suspend fun listenerForParams(): AppliedNetworkAction? {

    for (params in SetUserConfigurationChannel.appliedNetworkAction){
        return AppliedNetworkAction(params.appliedNetworkAction, params.params)
    }
    return null
}