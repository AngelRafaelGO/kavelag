package org.kavelag.project.parser

import org.junit.Assert
import org.junit.Test

class PostParserTest{

    private val postParser = PostParser()


    @Test
    fun regex_post_nominal_case(){
        //Given
        val request = """
        POST /api/data HTTP/1.1
        Content-Type: application/json
        Authorization: Bearer token123
        
        {"key": "value"}
    """.trimIndent()

        //When
        val result = postParser.isHttpRequest(request)

        //Then
        Assert.assertTrue(result)
    }


}