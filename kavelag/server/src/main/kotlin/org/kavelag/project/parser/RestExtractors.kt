package org.kavelag.project.parser

fun extractMethod(httpRequest: String): String? =
    runCatching {
        extractLinesFromRequest(httpRequest)?.let { RestRegex.methodRegex.find(it)?.value }
    }.onFailure {
        println("Error while parsing Method from request: ${it.message}")
    }.getOrNull()

fun extractHTTPProtocolVersion(httpRequest: String): String? =
    runCatching {
        extractLinesFromRequest(httpRequest)?.let { RestRegex.httpProtocolVersionRegex.find(it)?.groupValues?.get(0) }
    }.onFailure {
        println("Error while parsing HTTP Protocol Version from request: ${it.message}")
    }.getOrNull()

fun extractRequestedResourcePath(httpRequest: String): String? =
    runCatching {
        extractLinesFromRequest(httpRequest)?.let { RestRegex.requestedResourceRegex.find(it)?.groupValues?.get(1) }
    }.onFailure {
        println("Error while parsing Requested Resource from request: ${it.message}")
    }.getOrNull()


fun extractBody(httpRequest: String): String? {
    println(httpRequest)
    val result = RestRegex.bodyRegex.find(httpRequest)
    if (result != null) {
        val body = result.groups[1]?.value
        if (body != null && !body.contains("\n")) {
            return body
        } else if (body != null) {
            return body.lines().joinToString("") { it.trim() }
        }
    }
    return null
}

fun extractHeaders(httpRequest: String): MutableMap<String, String>? =
    runCatching {
        mutableMapOf<String, String>().apply {
            httpRequest.split("\r\n").forEach { requestLine ->
                if (requestLine.isBlank()) return@forEach
                if (RestRegex.flexibleHeaderRegex.matches(requestLine)) {
                    val (key, value) = requestLine.split(":", limit = 2)
                    this[key.trim()] = value.trim()
                }
            }
        }
    }.onFailure {
        println("Error while parsing Headers from request: ${it.message}")
    }.getOrNull()

/*
Since HTTP is a structured protocol, this function can return the line that contains the line
needed by the calling function
 */
fun extractLinesFromRequest(httpRequest: String): String? =
    runCatching {
        httpRequest.split("\r\n").getOrNull(0)
    }.onFailure {
        println("Error while extracting line from request: ${it.message}")
    }.getOrNull()
