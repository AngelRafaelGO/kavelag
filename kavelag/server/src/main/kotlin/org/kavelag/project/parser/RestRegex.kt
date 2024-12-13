package org.kavelag.project.parser

object RestRegex {

    val flexibleHeaderRegex: Regex = "^[a-zA-Z0-9-]+:\\s.*$".toRegex()
    val httpProtocolVersionRegex: Regex = "HTTP/([0-9]+\\.[0-9]+)".toRegex()

    // The requested resource regex works for both with and without query params requests
    val requestedResourceRegex: Regex = "^[A-Z]+\\s([\\/\\w\\.-]*(\\?.*)?)\\sHTTP/".toRegex()
    val methodRegex: Regex = "^[A-Z]+".toRegex()
    val bodyRegex: Regex = "(?s)\r\n\r\n(.*)".toRegex()
}
