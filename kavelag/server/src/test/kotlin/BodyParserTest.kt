import org.junit.jupiter.api.Assertions
import org.junit.Test
import org.junit.jupiter.api.DisplayName
import org.kavelag.project.parser.bodyParser

class BodyParserTest {

    @Test
    @DisplayName("bodyParser::Nominal case::Should return the body in String")
    fun bodyParser_case_1() {
        //Given
        val httpRequest = """
        POST /example HTTP/1.1
        Host: www.example.com
        Content-Type: application/json
        Content-Length: 27
        
        {
        "key": "value",
        "foo": "bar"
        }
                """.trimIndent()

        //When
        val actual = bodyParser(httpRequest)


        //Then
        Assertions.assertEquals(
            """{
"key": "value",
"foo": "bar"
}""".trimIndent(), actual
        )
    }

    @Test
    @DisplayName("bodyParser::Case with no body::Should return null")
    fun bodyParser_case_2() {
        //Given
        val httpRequest = """
        POST /example HTTP/1.1
        Host: www.example.com
        Content-Type: application/json
        Content-Length: 27
                """.trimIndent()

        //When
        val actual = bodyParser(httpRequest)


        //Then
        Assertions.assertNull(actual)
    }


}

