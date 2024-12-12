package org.kavelag.project.parser

fun extractMethod(httpRequest: String): String? {
    val httpMethod = extractLinesFromRequest(httpRequest)?.let { RestRegex.methodRegex.find(it) }
    return httpMethod?.value
}

fun extractHTTPProtocolVersion(httpRequest: String): String? {
    val httpProtocolVersion = extractLinesFromRequest(httpRequest)?.let { RestRegex.httpProtocolVersionRegex.find(it) }
    return httpProtocolVersion?.groupValues?.get(0)
}

fun extractRequestedResourceUrl(httpRequest: String): String? {
    val requestedResourceUrl = extractLinesFromRequest(httpRequest)?.let { RestRegex.requestedResourceRegex.find(it) }
    return requestedResourceUrl?.groupValues?.get(1)
}

fun extractHeaders(httpRequest: String): MutableMap<String, String> {
    val headers = mutableMapOf<String, String>()
    val requestLines = httpRequest.split("\r\n")
    for (requestLine in requestLines) {
        if (requestLine.isBlank()) break
        if (RestRegex.flexibleHeaderRegex.matches(requestLine)) {
            val (key, value) = requestLine.split(":", limit = 2)
            headers[key.trim()] = value.trim()
        }
    }
    return headers
}

/*
Since HTTP is a structured protocol, this function can return the line that contains the element
needed by the calling function
 */
fun extractLinesFromRequest(httpRequest: String): String? {
    val requestLines = httpRequest.split("\r\n")
    val requestLine = requestLines.getOrNull(0) ?: return null
    return requestLine
}