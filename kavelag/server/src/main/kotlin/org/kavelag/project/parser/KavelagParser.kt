package org.kavelag.project.parser

import org.kavelag.project.models.HttpRequest

fun parseIncomingHttpRequest(httpRequest: String): HttpRequest {
    val requestMethod = extractMethod(httpRequest)
    val requestRequestedResourcePath = extractRequestedResourcePath(httpRequest)
    val requestHTTPProtocolVersion = extractHTTPProtocolVersion(httpRequest)
    val requestHeaders = extractHeaders(httpRequest)

    if (requestMethod == "POST") {
        // TODO: we parse the body
    }

    val parsedRequest =
        HttpRequest(requestMethod!!, requestRequestedResourcePath!!, requestHTTPProtocolVersion!!, requestHeaders)
    return parsedRequest
}
