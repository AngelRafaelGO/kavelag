package org.kavelag.project

import kotlinx.serialization.Serializable

@Serializable
data class DestinationServerConfig(val url: String, val port: Int)

@Serializable
data class AppliedNetworkAction(val appliedNetworkAction: String)

@Serializable
data class HttpIncomingData(val httpIncomingData: String)

@Serializable
data class HttpDestinationServerResponse(val httpDestinationServerResponse: String)
