package org.kavelag.project.parser

fun paramsParser(httpRequest: String): String? {
    val paramsRegex = Regex("\\?([^ ]+)")

    val result = paramsRegex.find(httpRequest)
    if(result != null){
        return result.groups[1]?.value
    }
    return ""
}