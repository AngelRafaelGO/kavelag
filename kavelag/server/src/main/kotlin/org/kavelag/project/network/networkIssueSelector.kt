package org.kavelag.project.network

import kotlinx.coroutines.delay
import org.kavelag.project.models.AppliedNetworkAction
import kotlin.random.Random

suspend fun networkIssueSelectorOnConnect(actionToApply: AppliedNetworkAction): Boolean {
    val action = actionToApply.appliedNetworkAction.lowercase().trim()
    when (action) {
        "latency" -> networkLatency(actionToApply.readParam!!.toLong())
        "randomrequestfailure" -> return Random.nextBoolean()
        "nonetwork" -> return false
        "requestFailurePercent" -> return Random.nextDouble() < actionToApply.percentParam!!
    }
    return true
}

suspend fun networkIssueSelectorOnRead(actionToApply: AppliedNetworkAction): Boolean {
    val action = actionToApply.appliedNetworkAction.lowercase().trim()
    when (action) {
        "latency" -> networkLatency(actionToApply.connectParam!!.toLong())
    }
    return true
}

private suspend fun networkLatency(delay: Long) {
    delay(delay)
}
