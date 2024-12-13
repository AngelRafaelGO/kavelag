package org.kavelag.project.parser

import org.kavelag.project.models.HttpRequest

fun parseIncomingHttpRequest(httpRequest: String): HttpRequest {
    val requestMethod = extractMethod(httpRequest)
    val requestRequestedResourcePath = extractRequestedResourcePath(httpRequest)
    val requestHTTPProtocolVersion = extractHTTPProtocolVersion(httpRequest)
    val requestHeaders = extractHeaders(httpRequest)
    val requestBody = extractBody(httpRequest)

    if (requestMethod == "POST") {
        try {
            val parsedRequest =
                HttpRequest(requestMethod, requestRequestedResourceAddress!!, requestHTTPProtocolVersion!!, requestHeaders, requestBody)
            return parsedRequest
        }catch (e: Exception){
            println(e)
        }
    }

    val parsedRequest =
        HttpRequest(requestMethod!!, requestRequestedResourcePath!!, requestHTTPProtocolVersion!!, requestHeaders)
    return parsedRequest
}
