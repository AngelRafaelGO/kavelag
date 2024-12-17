package org.kavelag.project.listener

import org.kavelag.project.AppliedNetworkAction
import org.kavelag.project.SetUserConfigurationChannel

suspend fun listenerForParams(): AppliedNetworkAction{
    var action = ""
    var param = 0
    for (params in SetUserConfigurationChannel.appliedNetworkAction){
        action = params.appliedNetworkAction
        param = params.params
        println("Action: $action, Parameter: $param")

    }
    return AppliedNetworkAction(action, param)
}