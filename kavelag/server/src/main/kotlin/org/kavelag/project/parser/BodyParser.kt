package org.kavelag.project.parser

fun bodyParser(httpRequest: String): String? {

    val bodyRegex = RestRegex.bodyRegex

    val result = bodyRegex.find(httpRequest)
    if (result != null) {
        return result.groups[1]?.value
    }
    return null
}

