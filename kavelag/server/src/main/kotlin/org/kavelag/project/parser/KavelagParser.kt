package org.kavelag.project.parser

fun parserDispatcher(httpRequest: String): String {
    println("String to parse: \n$httpRequest")
    val requestMethod = extractMethodFromRequest(httpRequest);
    val requestHeaders = extractHeadersFromRequest(httpRequest);
    println(requestMethod)
//    val request = HttpRequest();


    return httpRequest
}

fun extractMethodFromRequest(httpRequest: String): String? {
    if (RestRegex.getRegex.find(httpRequest)?.value == "GET") {
        return "GET"
    } else if (RestRegex.postRegex.find(httpRequest)?.value == "POST") {
        return "POST"
    }
    return null
}

fun extractHeadersFromRequest(httpRequest: String): List<String>? {
    val currentHeader = RestRegex.headerRegex.find(httpRequest)
    return null
}