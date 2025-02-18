import org.kavelag.project.parser.parseIncomingHttpRequest
import kotlin.test.Test
import kotlin.test.assertEquals

class KavelagParserTest {
    companion object {
        const val HTTP_GET_REQUEST_WITH_QUERY_PARAMS =
            "GET /endpoint/child-endpoint/child-endpoint/resource?key1=value1&key2=value2&key3=value3&key4=value4&key5=value5 HTTP/1.1\r\nHost: example.com\r\nAuthorization: Bearer abcdef123456\r\nAccept: application/json\r\nUser-Agent: CustomClient/1.0\r\n\r\n"
        const val HTTP_GET_REQUEST_WITHOUT_QUERY_PARAMS =
            "GET /endpoint/child-endpoint/child-endpoint/resource HTTP/1.1\r\nHost: example.com\r\nAuthorization: Bearer abcdef123456\r\nAccept: application/json\r\nUser-Agent: CustomClient/1.0\r\n\r\n"
        const val HTTP_POST_REQUEST_WITH_BODY =
            "POST /endpoint HTTP/1.1\r\nHost: example.com\r\nContent-Type: application/json\r\nAuthorization: Bearer abcdef123456\r\nAccept: application/json\r\nUser-Agent: CustomClient/1.0\r\nContent-Length: 87\r\n\r\n{\"key1\":\"value1\",\"key2\":\"value2\",\"key3\":\"value3\",\"key4\":\"value4\",\"key5\":\"value5\"}\n"
        const val HTTP_POST_REQUEST_WITHOUT_BODY =
            "POST /endpoint HTTP/1.1\r\nHost: example.com\r\nContent-Type: application/json\r\nAuthorization: Bearer abcdef123456\r\nAccept: application/json\r\nUser-Agent: CustomClient/1.0\r\nContent-Length: 87\r\n\r\n"

    }

    @Test
    fun shouldParseHTTPGetRequestWithHeadersWithQueryParams() {
        // ACT
        val actual = parseIncomingHttpRequest(HTTP_GET_REQUEST_WITH_QUERY_PARAMS)

        // ASSERT
        assertEquals("GET", actual.method)
        assertEquals(
            "/endpoint/child-endpoint/child-endpoint/resource?key1=value1&key2=value2&key3=value3&key4=value4&key5=value5",
            actual.requestedResource
        )
        assertEquals("HTTP/1.1", actual.httpProtocolVersion)
        assertEquals(
            mutableMapOf(
                "Host" to "example.com",
                "Authorization" to "Bearer abcdef123456",
                "Accept" to "application/json",
                "User-Agent" to "CustomClient/1.0"
            ), actual.headers
        )
        assertEquals(null, actual.body)
    }

    @Test
    fun shouldParseHTTPGetRequestWithoutHeadersWithoutQueryParams() {
        // ACT
        val actual = parseIncomingHttpRequest(HTTP_GET_REQUEST_WITHOUT_QUERY_PARAMS)

        // ASSERT
        assertEquals("GET", actual.method)
        assertEquals(
            "/endpoint/child-endpoint/child-endpoint/resource",
            actual.requestedResource
        )
        assertEquals("HTTP/1.1", actual.httpProtocolVersion)
        assertEquals(
            mutableMapOf(
                "Host" to "example.com",
                "Authorization" to "Bearer abcdef123456",
                "Accept" to "application/json",
                "User-Agent" to "CustomClient/1.0"
            ), actual.headers
        )
        assertEquals(null, actual.body)
    }

    @Test
    fun shouldParseHTTPPostRequestWithHeadersWithBody() {
        // ACT
        val actual = parseIncomingHttpRequest(HTTP_POST_REQUEST_WITH_BODY)

        // ASSERT
        assertEquals("POST", actual.method)
        assertEquals(
            "/endpoint",
            actual.requestedResource
        )
        assertEquals("HTTP/1.1", actual.httpProtocolVersion)
        assertEquals(
            mutableMapOf(
                "Host" to "example.com",
                "Content-Type" to "application/json",
                "Authorization" to "Bearer abcdef123456",
                "Accept" to "application/json",
                "User-Agent" to "CustomClient/1.0",
                "Content-Length" to "87"
            ), actual.headers
        )
        assertEquals(
            "{\"key1\":\"value1\",\"key2\":\"value2\",\"key3\":\"value3\",\"key4\":\"value4\",\"key5\":\"value5\"}",
            actual.body
        )
    }

    @Test
    fun shouldParseHTTPPostWithHeadersWithoutBody() {
        // ACT
        val actual = parseIncomingHttpRequest(HTTP_POST_REQUEST_WITHOUT_BODY)

        // ASSERT
        assertEquals("POST", actual.method)
        assertEquals(
            "/endpoint",
            actual.requestedResource
        )
        assertEquals("HTTP/1.1", actual.httpProtocolVersion)
        assertEquals(
            mutableMapOf(
                "Host" to "example.com",
                "Content-Type" to "application/json",
                "Authorization" to "Bearer abcdef123456",
                "Accept" to "application/json",
                "User-Agent" to "CustomClient/1.0",
                "Content-Length" to "87"
            ), actual.headers
        )
        assertEquals(
            null,
            actual.body
        )
    }
}
