package org.kavelag.project.parser

fun bodyParser(httpRequest: String): String? {

    val bodyRegex = Regex("(?s)\n\n(.*)")

    val result = bodyRegex.find(httpRequest)
    if (result != null) {
        return result?.groups?.get(1)?.value
    }
    return null
}

