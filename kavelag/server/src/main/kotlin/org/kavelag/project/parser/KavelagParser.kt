package org.kavelag.project.parser

fun parserDispatcher(httpRequest: String): String {
    println("String to parse: $httpRequest")
    val inputToArray = httpRequest.split(" ")
    if (inputToArray.size < 3) {
        throw IllegalArgumentException("The http request is not processable")
    }

    if (inputToArray[0] == "GET") {
        println("Its a GET !")
    } else if (inputToArray[0] == "POST") {
        println("Its a POST !")
    }

    return httpRequest
}