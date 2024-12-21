package org.kavelag.project.network

import kotlinx.coroutines.delay
import org.kavelag.project.models.AppliedNetworkAction

suspend fun networkIssueSelector(param: AppliedNetworkAction, count: Int) {
    val action = param.appliedNetworkAction.lowercase().trim()

    println("networkIssueSelector::action: $action, param: $param")
    when (action) {
        "latency" -> networkLatency(param.params!!.toLong())
        "1on2" -> oneRequestFailsOver2(count)
        "nonetwork" -> noNetwork()
    }
}
class NoNetworkException(message: String) : Exception(message)

private fun noNetwork() {
    throw NoNetworkException("No network available")
}

private suspend fun networkLatency(delay: Long) {
    delay(delay)
}

fun oneRequestFailsOver2(count: Int): Boolean {
    println("1on2: $count")
    return if (count % 2 == 0) {
        println("request can not be sent")
        false
    } else {
        println("Request sent!!!")
        true
    }
}