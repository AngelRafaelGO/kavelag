package org.kavelag.project.models

data class AppliedNetworkAction(val appliedNetworkAction: String, val params: Int? = null)
data class ProxySocketConfiguration(val url: String, val port: IntArray, val appliedNetworkAction: AppliedNetworkAction)

