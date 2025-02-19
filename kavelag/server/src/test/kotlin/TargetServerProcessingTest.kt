import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.test.runTest
import org.kavelag.project.models.HttpRequest
import org.kavelag.project.targetServerProcessing.callTargetServer
import org.kavelag.project.targetServerProcessing.getMethod
import org.kavelag.project.targetServerProcessing.isValidUrl
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TargetServerProcessingTest {
    @Test
    fun shouldValidateUrls() {
        // ACT & ASSERT
        assertTrue(isValidUrl("http://kavelag.com"))
    }

    @Test
    fun shouldCallTargetServerWithGETRequest() = runTest {
        // ARRANGE
        val expectedTargetServerResponse =
            "HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n\r\n{\"message\":\"Success\"}"
        val mockHttpRequest = HttpRequest(
            method = "GET",
            requestedResource = "/api/data",
            httpProtocolVersion = "HTTP/1.1",
            headers = mutableMapOf("Content-Type" to "application/json"),
            body = null
        )

        mockkStatic(::getMethod)
        coEvery { getMethod("http://test.com", 8080, mockHttpRequest) } returns expectedTargetServerResponse

        // Act
        val result = callTargetServer("http://test.com", 8080, mockHttpRequest)

        // Assert
        assertEquals(expectedTargetServerResponse, result)
        coVerify(exactly = 1) { getMethod("http://test.com", 8080, mockHttpRequest) }

        unmockkStatic(::getMethod)
    }
}