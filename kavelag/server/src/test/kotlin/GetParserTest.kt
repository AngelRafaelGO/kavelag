import org.junit.Test
import org.kavelag.project.parser.parseGetRequest

class GetParserTest() {
    @Test
    fun shouldParseGetRequest() {
        // ARRANGE
        val getRequest = "GET / HTTP/1.1"
        val destinationAddress = "http:/1.1"

        // ACT
        val parsedGetResponse = parseGetRequest(getRequest)

        // ASSERT
        
    }
}