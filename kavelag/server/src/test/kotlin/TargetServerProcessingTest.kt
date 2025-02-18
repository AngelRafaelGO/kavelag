import junit.framework.TestCase.assertTrue

import org.kavelag.project.targetServerProcessing.isValidUrl
import kotlin.test.Test

class TargetServerProcessingTest {
    @Test
    fun shouldReturnTrueIfValidURL() {
        // ACT & ASSERT
        assertTrue(isValidUrl("http://mysiteurl.com"))
    }
}