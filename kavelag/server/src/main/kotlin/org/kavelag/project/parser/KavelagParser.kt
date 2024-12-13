package org.kavelag.project.parser

import org.kavelag.project.models.HttpRequest

fun parseIncomingHttpRequest(httpRequest: String): HttpRequest {
    val requestMethod = extractMethod(httpRequest)
    val requestRequestedResourceAddress = extractRequestedResourceUrl(httpRequest)
    val requestHTTPProtocolVersion = extractHTTPProtocolVersion(httpRequest)
    val requestHeaders = extractHeaders(httpRequest)
    val requestBody = extractBody(httpRequest)
    val requestParams = extractParams(httpRequest)

    if (requestMethod == "POST") {
        try {
            val parsedRequest =
                HttpRequest(requestMethod, requestRequestedResourceAddress!!, requestParams, requestHTTPProtocolVersion!!, requestHeaders, requestBody)
            return parsedRequest
        }catch (e: Exception){
            println(e)
        }
    }

    val parsedRequest =
        HttpRequest(requestMethod!!, requestRequestedResourceAddress!!, requestParams, requestHTTPProtocolVersion!!, requestHeaders)
    return parsedRequest
}
