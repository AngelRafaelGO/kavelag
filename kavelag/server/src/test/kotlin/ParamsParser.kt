import org.junit.Assert
import org.junit.Test
import org.kavelag.project.parser.extractParams

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

        val expected = mapOf(
            "q" to "kotlin",
            "lang" to "fr"
        )

        //When
        val actual = extractParams(request)

        //Then
        Assert.assertEquals(expected, actual)
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

        val expected = emptyMap<String, String>()

        //When
        val actual = extractParams(request)

        //Then
        Assert.assertEquals(expected, actual)
    }


}