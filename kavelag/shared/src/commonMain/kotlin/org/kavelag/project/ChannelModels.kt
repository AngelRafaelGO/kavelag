package org.kavelag.project

import kotlinx.serialization.Serializable

@Serializable
data class HttpIncomingData(val httpIncomingData: String)

@Serializable
data class HttpDestinationServerResponse(val httpDestinationServerResponse: String)

@Serializable
data class ProxyGenericInfo(val proxyGenericInfo: String)

@Serializable
data class Timer(val timer: String)