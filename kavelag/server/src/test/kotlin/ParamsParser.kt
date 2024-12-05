import org.junit.Assert
import org.junit.Test
import org.kavelag.project.parser.paramsParser

class ParamsParser {

    @Test
    fun paramsParser_case1() {
        //Given
        val request = """
        POST /search?q=kotlin&lang=fr HTTP/1.1
        Host: www.example.com
        Content-Type: application/json
        Content-Length: 27


        {
            "key": "value",
            "foo": "bar"
        }
        """.trimIndent()
        //When
        val actual = paramsParser(request)

        //Then
        Assert.assertEquals("q=kotlin&lang=fr", actual)
    }

    @Test
    fun paramsParser_case2() {
        //Given
        val request = """
        POST /search HTTP/1.1
        Host: www.example.com
        Content-Type: application/json
        Content-Length: 27


        {
            "key": "value",
            "foo": "bar"
        }
        """.trimIndent()
        //When
        val actual = paramsParser(request)

        //Then
        Assert.assertEquals("", actual)
    }


}