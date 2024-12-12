import org.junit.Test
import org.kavelag.project.parser.*
import kotlin.test.assertEquals

class RestExtractorsTest() {
    companion object {
        const val HTTP_GET_REQUEST =
            "GET /endpoint/child-endpoint/child-endpoint/resource?key1=value1&key2=value2&key3=value3&key4=value4&key5=value5 HTTP/1.1\r\nHost: example.com\r\nAuthorization: Bearer abcdef123456\r\nAccept: application/json\r\nUser-Agent: CustomClient/1.0\r\n\r\n"
        const val HTTP_POST_REQUEST =
            "POST /endpoint HTTP/1.1\r\nHost: example.com\r\nContent-Type: application/json\r\nAuthorization: Bearer abcdef123456\r\nAccept: application/json\r\nUser-Agent: CustomClient/1.0\r\nContent-Length: 87\r\n\r\n{\"key1\":\"value1\",\"key2\":\"value2\",\"key3\":\"value3\",\"key4\":\"value4\",\"key5\":\"value5\"}\n"
        const val HTTP_START_LINE =
            "GET /endpoint/child-endpoint/child-endpoint/resource?key1=value1&key2=value2&key3=value3&key4=value4&key5=value5 HTTP/1.1"
        const val HTTP_HEADERS = "Host: example.com\n" +
                "Authorization: Bearer abcdef123456\n" +
                "Accept: application/json\n" +
                "User-Agent: CustomClient/1."
    }

    @Test
    fun shouldExtractHTTPGetMethodFromRequest() {
        // ACT
        val actual = extractMethod(HTTP_GET_REQUEST)

        // ASSERT
        assertEquals(actual, "GET")
    }

    @Test
    fun shouldExtractHTTPPostMethodFromRequest() {
        // ACT
        val actual = extractMethod(HTTP_POST_REQUEST)

        // ASSERT
        assertEquals(actual, "POST")
    }

    @Test
    fun shouldExtractHTTPProtocolVersionFromRequest() {
        // ACT
        val actual = extractHTTPProtocolVersion(HTTP_GET_REQUEST)

        // ASSERT
        assertEquals(actual, "HTTP/1.1")
    }

    @Test
    fun shouldExtractRequestedResourcePathFromRequest() {
        // ACT
        val actual = extractRequestedResourceUrl(HTTP_GET_REQUEST)

        // ASSERT
        assertEquals(actual, "/endpoint/child-endpoint/child-endpoint/resource")
    }

    @Test
    fun shouldExtractHeadersFromRequest() {
        // ARRANGE
        val expected: MutableMap<String, String> = mutableMapOf(
            "Host" to "example.com",
            "Authorization" to "Bearer abcdef123456",
            "Accept" to "application/json",
            "User-Agent" to "CustomClient/1.0"
        )
        
        // ACT
        val actual = extractHeaders(HTTP_GET_REQUEST)

        // ASSERT
        assertEquals(actual, expected)
    }

    @Test
    fun shouldRetrieveCorrectLineFromRequestWhenCallingForMethod() {
        // ACT
        val actual = extractLinesFromRequest(HTTP_GET_REQUEST)

        // ASSERT
        assertEquals(actual, HTTP_START_LINE)
    }

    @Test
    fun shouldRetrieveCorrectLineFromRequestWhenCallingForHTTPProtocolVersion() {
        // ACT
        val actual = extractLinesFromRequest(HTTP_GET_REQUEST)

        // ASSERT
        assertEquals(actual, HTTP_START_LINE)

    }

    @Test
    fun shouldRetrieveCorrectLineFromRequestWhenCallingForRequestedResource() {
        // ACT
        val actual = extractLinesFromRequest(HTTP_GET_REQUEST)

        // ASSERT
        assertEquals(actual, HTTP_START_LINE)
    }

    @Test
    fun shouldRetrieveCorrectLineFromRequestWhenCallingForHeaders() {
        // ACT
        val actual = extractLinesFromRequest(HTTP_GET_REQUEST)

        // ASSERT
        assertEquals(actual, HTTP_HEADERS)
    }
}
