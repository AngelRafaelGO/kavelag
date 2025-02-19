import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.test.runTest
import org.kavelag.project.models.HttpRequest
import org.kavelag.project.targetServerProcessing.callTargetServer
import org.kavelag.project.targetServerProcessing.getMethod
import org.kavelag.project.targetServerProcessing.isValidUrl
import org.kavelag.project.targetServerProcessing.postMethod
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
        val actual = callTargetServer("http://test.com", 8080, mockHttpRequest)

        // Assert
        assertEquals(expectedTargetServerResponse, actual)
        coVerify(exactly = 1) { getMethod("http://test.com", 8080, mockHttpRequest) }

        unmockkStatic(::getMethod)
    }

    @Test
    fun shouldCallTargetServerWithPOSTRequest() = runTest {
        //ARRANGE
        val expectedTargetServerResponse =
            """HTTP/1.1 200 OK
            Content-Type: application/json
            
            {
            "status": "success",
            "message": "Requête traitée avec succès",
            "data": {
                "id": 123,
                "name": "Exemple",
                "created_at": "2025-02-19T12:00:00Z"
            }
        }"""
        val mockHttpRequest = HttpRequest(
            method = "POST",
            requestedResource = "/api/data",
            httpProtocolVersion = "HTTP/1.1",
            headers = mutableMapOf("Content-Type" to "application/json"),
            body = """{
                "status": "success",
                "message": "Requête traitée avec succès",
                "data": {
                    "id": 123,
                    "name": "Exemple",
                    "created_at": "2025-02-19T12:00:00Z"
                }
            }"""
        )

        mockkStatic(::postMethod)
        coEvery { postMethod("http://test.com", 8080, mockHttpRequest) } returns expectedTargetServerResponse

        // Act
        val actual = callTargetServer("http://test.com", 8080, mockHttpRequest)
        
        // Assert
        assertEquals(expectedTargetServerResponse, actual)
        coVerify(exactly = 1) { postMethod("http://test.com", 8080, mockHttpRequest) }

        unmockkStatic(::postMethod)
    }
}