package org.kavelag.project.models

data class AppliedNetworkAction(
    val appliedNetworkAction: String,
    val connectParam: Int? = null,
    val responseParam: Int? = null
)

data class ProxySocketConfiguration(val url: String, val port: Int, val appliedNetworkAction: AppliedNetworkAction)

