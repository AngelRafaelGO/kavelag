import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.kavelag.project.models.HttpRequest
import org.kavelag.project.targetServerProcessing.callTargetServer
import org.kavelag.project.targetServerProcessing.getMethod
import org.kavelag.project.targetServerProcessing.isValidUrl
import kotlin.test.Test
import kotlin.test.assertEquals

class TargetServerProcessingTest {
    @ParameterizedTest
    @CsvSource(
        "http://mysiteurl.com, true",
        "https://google.com, true",
        "http://sub.domain.com/path, true",
        "ftp://invalid.com, false",
        "htp://wrong.com, false",
        "http://, false",
        "invalid_url, false",
        "https://192.168.1.1, true",
//        "http://localhost:8080, true"
    )
    fun shouldValidateUrls(inputUrl: String, expectedResult: Boolean) {
        // ACT & ASSERT
        assertEquals(expectedResult, isValidUrl(inputUrl))
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