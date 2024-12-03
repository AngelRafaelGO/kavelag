package org.kavelag.project.parser

class PostParser {

//    fun parser(httpRequest: String): String{
//
//    }

    fun isHttpRequest(httpRequest: String): Boolean{
        val postRegex = """(?i)POST\s+\/[^\s]*\s+HTTP\/\d\.\d\r?\n(?:[a-zA-Z0-9\-]+:\s[^\r\n]+\r?\n)*\r?\n(.+)""".toRegex()
        val result = postRegex.find(httpRequest)

        return result != null

    }

}