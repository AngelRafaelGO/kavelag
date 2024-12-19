package org.kavelag.project.models

data class AppliedNetworkAction(val appliedNetworkAction: String, val params: Int)
data class ProxySocketConfiguration(val url: String, val port: Int, val appliedNetworkAction: AppliedNetworkAction)

