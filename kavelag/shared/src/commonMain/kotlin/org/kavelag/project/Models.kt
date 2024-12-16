package org.kavelag.project

import kotlinx.serialization.Serializable

@Serializable
data class UserProxyConfig(val url: String, val port: String)
