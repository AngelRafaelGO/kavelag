package org.kavelag.project.parser

import org.kavelag.project.models.HttpRequest

fun parserDispatcher(httpRequest: String): HttpRequest {
    val requestMethod = extractMethod(httpRequest)
    val requestRequestedResourceAddress = extractRequestedResourceUrl(httpRequest)
    val requestHTTPProtocolVersion = extractHTTPProtocolVersion(httpRequest)
    val requestHeaders = extractHeaders(httpRequest)

    if (requestMethod == "POST") {
        // TODO: we parse the body
    }

    val parsedRequest =
        HttpRequest(requestMethod!!, requestRequestedResourceAddress!!, requestHTTPProtocolVersion!!, requestHeaders)
    return parsedRequest
}
