import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.kavelag.project.parser.*
import kotlin.test.assertEquals

class RestExtractorsTest() {
    companion object {
        const val HTTP_GET_REQUEST =
            "GET /endpoint/child-endpoint/child-endpoint/resource HTTP/1.1\r\nHost: example.com\r\nAuthorization: Bearer abcdef123456\r\nAccept: application/json\r\nUser-Agent: CustomClient/1.0\r\n\r\n"
        const val HTTP_GET_REQUEST_WITH_QUERY_PARAMS =
            "GET /endpoint/child-endpoint/child-endpoint/resource?key1=value1&key2=value2&key3=value3&key4=value4&key5=value5 HTTP/1.1\r\nHost: example.com\r\nAuthorization: Bearer abcdef123456\r\nAccept: application/json\r\nUser-Agent: CustomClient/1.0\r\n\r\n"
        const val HTTP_POST_REQUEST_WITH_SINGLE_LINE_BODY =
            "POST /endpoint HTTP/1.1\r\nHost: example.com\r\nContent-Type: application/json\r\nAuthorization: Bearer abcdef123456\r\nAccept: application/json\r\nUser-Agent: CustomClient/1.0\r\nContent-Length: 87\r\n\r\n{\"key1\":\"value1\",\"key2\":\"value2\",\"key3\":\"value3\",\"key4\":\"value4\",\"key5\":\"value5\"}\n"
        const val HTTP_START_LINE =
            "GET /endpoint/child-endpoint/child-endpoint/resource?key1=value1&key2=value2&key3=value3&key4=value4&key5=value5 HTTP/1.1"
        const val HTTP_REQUESTED_RESOURCE_WITH_QUERY_PARAMS =
            "/endpoint/child-endpoint/child-endpoint/resource?key1=value1&key2=value2&key3=value3&key4=value4&key5=value5"
        const val HTTP_REQUESTED_RESOURCE_WITHOUT_QUERY_PARAMS =
            "/endpoint/child-endpoint/child-endpoint/resource"
        const val HTTP_EXPECTED_SINGLE_LINE_BODY =
            "{\"key1\":\"value1\",\"key2\":\"value2\",\"key3\":\"value3\",\"key4\":\"value4\",\"key5\":\"value5\"}"
        const val HTTP_POST_REQUEST_WITH_MULTILINE_BODY =
            "POST /endpoint HTTP/1.1\r\n" +
                    "Host: example.com\r\n" +
                    "Content-Type: application/json\r\n" +
                    "Authorization: Bearer abcdef123456\r\n" +
                    "Accept: application/json\r\n" +
                    "User-Agent: CustomClient/1.0\r\n" +
                    "Content-Length: 87\r\n" +
                    "\r\n" +
                    "{\r\n" +
                    "  \"key1\":\"value1\",\r\n" +
                    "  \"key2\":\"value2\",\r\n" +
                    "  \"key3\":\"value3\",\r\n" +
                    "  \"key4\":\"value4\",\r\n" +
                    "  \"key5\":\"value5\"\r\n" +
                    "}\r\n"
    }

    @Test
    fun shouldExtractHTTPGetMethodFromRequest() {
        // ACT
        val actual = extractMethod(HTTP_GET_REQUEST)

        // ASSERT
        assertEquals("GET", actual)
    }

    @Test
    fun shouldExtractHTTPPostMethodFromRequest() {
        // ACT
        val actual = extractMethod(HTTP_POST_REQUEST_WITH_SINGLE_LINE_BODY)

        // ASSERT
        assertEquals("POST", actual)
    }

    @Test
    fun shouldExtractHTTPProtocolVersionFromRequest() {
        // ACT
        val actual = extractHTTPProtocolVersion(HTTP_GET_REQUEST)

        // ASSERT
        assertEquals("HTTP/1.1", actual)
    }

    @Test
    fun shouldExtractRequestedResourcePathFromRequestWithQueryParams() {
        // ACT
        val actual = extractRequestedResourcePath(HTTP_GET_REQUEST_WITH_QUERY_PARAMS)

        // ASSERT
        assertEquals(HTTP_REQUESTED_RESOURCE_WITH_QUERY_PARAMS, actual)
    }

    @Test
    fun shouldExtractRequestedResourcePathFromRequestWithoutQueryParams() {
        // ACT
        val actual = extractRequestedResourcePath(HTTP_GET_REQUEST)

        // ASSERT
        assertEquals(HTTP_REQUESTED_RESOURCE_WITHOUT_QUERY_PARAMS, actual)
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
        assertEquals(expected, actual)
    }

    @Test
    fun shouldRetrieveCorrectLineFromRequestWhenCallingForMethod() {
        // ACT
        val actual = extractLinesFromRequest(HTTP_GET_REQUEST_WITH_QUERY_PARAMS)

        // ASSERT
        assertEquals(HTTP_START_LINE, actual)
    }

    @Test
    fun shouldRetrieveCorrectLineFromRequestWhenCallingForHTTPProtocolVersion() {
        // ACT
        val actual = extractLinesFromRequest(HTTP_GET_REQUEST_WITH_QUERY_PARAMS)

        // ASSERT
        assertEquals(HTTP_START_LINE, actual)
    }

    @Test
    fun shouldRetrieveCorrectLineFromRequestWhenCallingForRequestedResource() {
        // ACT
        val actual = extractLinesFromRequest(HTTP_GET_REQUEST_WITH_QUERY_PARAMS)

        // ASSERT
        assertEquals(HTTP_START_LINE, actual)
    }

    @Test
    fun shouldRetrieveBodyFromRequestWithMultilineBodyWhenPresent() {
        // Act
        val actual = extractBody(HTTP_POST_REQUEST_WITH_MULTILINE_BODY)
        println(actual)

        // Then
        Assertions.assertEquals(HTTP_EXPECTED_SINGLE_LINE_BODY, actual)
    }

    @Test
    fun shouldRetrieveBodyFromRequestWithSingleLineBodyWhenPresent() {
        // Given & When
        val actual = extractBody(HTTP_POST_REQUEST_WITH_SINGLE_LINE_BODY)
        println(actual)

        // Then
        Assertions.assertEquals(HTTP_EXPECTED_SINGLE_LINE_BODY, actual)
    }

    @Test
    fun shouldReturnNullWhenRequestHasNoBody() {
        // Given & When
        val actual = extractBody(HTTP_GET_REQUEST)

        //Then
        Assertions.assertEquals(null, actual)
    }
}
