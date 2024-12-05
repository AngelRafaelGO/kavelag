package org.kavelag.project.parser

import java.net.URLDecoder

fun paramsParser(httpRequest: String): Map<String, String> {
    val paramsRegex = RestRegex.paramsRegex
    val params = mutableMapOf<String, String>()

    val result = paramsRegex.findAll(httpRequest)

    result.forEach {match ->
        val key = match.groups[1]?.value
        val value = match.groups[2]?.value
        if (key != null && value != null) {
            params[URLDecoder.decode(key, "UTF-8")] = URLDecoder.decode(value, "UTF-8")
        }
    }

    return params
}