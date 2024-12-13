package org.kavelag.project.models

class HttpRequest(
    val method: String,
    val requestedResource: String,
    val requestParams: MutableMap<String, String>,
    val httpProtocolVersion: String,
    val headers: MutableMap<String, String>,
    val body: String? = null
)