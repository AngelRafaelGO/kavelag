package org.kavelag.project.network

import kotlinx.coroutines.delay
import org.kavelag.project.models.AppliedNetworkAction
import org.kavelag.project.models.NetworkException

suspend fun networkIssueSelector(param: AppliedNetworkAction, count: Int) {
    val action = param.appliedNetworkAction.lowercase().trim()

    println("networkIssueSelector::action: $action, param: $param")
    when (action) {
        "latency" -> networkLatency(param.params!!.toLong())
        "1on2" -> oneRequestFailsOver2(count)
        "nonetwork" -> noNetwork()
    }
}

//TODO: Find a good error to return
private fun noNetwork() {
    throw NetworkException("No network available")
}

private suspend fun networkLatency(delay: Long) {
    delay(delay)
}

fun oneRequestFailsOver2(count: Int) {
    println("1on2: $count")
    if (count % 2 == 0) {
        noNetwork()
    } else {
        println("Request sent!!!")
    }
}