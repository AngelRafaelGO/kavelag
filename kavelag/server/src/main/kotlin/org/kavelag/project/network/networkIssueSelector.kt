package org.kavelag.project.network

import kotlinx.coroutines.delay
import org.kavelag.project.models.AppliedNetworkAction
import kotlin.random.Random

suspend fun networkIssueSelector(param: AppliedNetworkAction): Boolean {
    val action = param.appliedNetworkAction.lowercase().trim()

    println("networkIssueSelector::action: $action, param: $param")
    when (action) {
        "latency" -> networkLatency(param.params!!.toLong())
        "randomrequestfailure" -> {
            return Random.nextBoolean()
        }

        "nonetwork" -> return false
    }
    return true
}

private suspend fun networkLatency(delay: Long) {
    delay(delay)
}