import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.kavelag.project.models.HttpRequest
import org.kavelag.project.targetServerProcessing.callTargetServer
import org.kavelag.project.targetServerProcessing.isValidUrl
import kotlin.test.Test

class TargetServerProcessingTest {
    @Test
    fun shouldReturnTrueIfValidURL() {
        // ACT & ASSERT
        assertTrue(isValidUrl("http://mysiteurl.com"))
    }

    @Test
    fun shouldCallTargetServerGetMethod() = runBlocking {
        // ARRANGE
        val headers: MutableMap<String, String> = mutableMapOf("test" to "header1", "test2" to "header2")
        val url = "http://localhost"
        val port = 8080
        val request = HttpRequest(
            method = "GET",
            requestedResource = "/test",
            httpProtocolVersion = "1.1",
            headers = headers
        )
        // ACT
        val actual = callTargetServer(url, port, request)
        println(actual)
        // ASSERT
        //val expectedResponse = "HTTP/1.1 200 OK\r\n\r\n"
        //assertEquals(expectedResponse, actual)
    }
}