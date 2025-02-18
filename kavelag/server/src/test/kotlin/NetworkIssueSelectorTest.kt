import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.kavelag.project.models.AppliedNetworkAction
import org.kavelag.project.network.networkIssueSelectorOnRead
import kotlin.test.Test

class NetworkIssueSelectorTest {
    @Test
    fun shouldApplyNetworkIssueOnRead() = runBlocking {
        val action = AppliedNetworkAction("latency", 2000, 0)
        val result = networkIssueSelectorOnRead(action)
        assertTrue(result)
    }
}