package org.kavelag.project.parser

import org.kavelag.project.models.HttpRequest

fun parseIncomingHttpRequest(httpRequest: String): HttpRequest {
    val requestMethod = extractMethod(httpRequest)
    val requestRequestedResourcePath = extractRequestedResourcePath(httpRequest)
    val requestHTTPProtocolVersion = extractHTTPProtocolVersion(httpRequest)
    val requestHeaders = extractHeaders(httpRequest)
    val requestBody = extractBody(httpRequest)
    val parsedRequest =
        HttpRequest(
            requestMethod!!,
            requestRequestedResourcePath!!,
            requestHTTPProtocolVersion!!,
            requestHeaders!!,
            requestBody
        )
    return parsedRequest
}
