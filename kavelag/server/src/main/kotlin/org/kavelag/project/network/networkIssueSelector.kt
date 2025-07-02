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
//        "requestFailurePercent" -> return Random.nextDouble() < actionToApply.percentParam!!
        "requestFailurePercent" -> return requestFailurePercent(10, 100)
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

private suspend fun requestFailurePercent(nbRequests: Int, percentageFailure : Int): Boolean{
    val nbFailedRequests = (nbRequests * percentageFailure) / 100

    val failedRequests = (0 until nbRequests).shuffled().take(nbFailedRequests).toSet()

    return true
}

fun main() {
    val nbRequests = 10 // from IHM
    val percentage = 100 // from IHM

    val nbFailedRequests = (nbRequests * percentage) / 100 //OK

    val requestReceived = "request"

    val requests = List(nbRequests) {requestReceived}

    val failedRequests = (0 until nbRequests).shuffled().take(nbFailedRequests).toSet()

    for (index in failedRequests){
        println(index)
    }

    println(requests)

//    for((index, request) in requests.withIndex()){
//        if (index in failedRequests){
//            println("$request false")
//        }else {
//            println("$request true")
//        }
//    }
}