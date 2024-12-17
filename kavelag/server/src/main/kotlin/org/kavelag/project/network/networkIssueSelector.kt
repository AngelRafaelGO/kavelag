package org.kavelag.project.network

import kotlinx.coroutines.delay
import org.kavelag.project.listener.listenerForParams

suspend fun networkIssueSelector(){
    val paramFromListener = listenerForParams()
    val action = paramFromListener.appliedNetworkAction.lowercase().trim()
    val param = paramFromListener.params

    when(action){
        "latency" -> networkLatency(param.toLong())
    }
}

private fun noNetwork(){
    return
    //No request sent
}

private suspend fun networkLatency(delay: Long){
    delay(delay)
}

private fun oneRequestFailsOver2(count: Int, requestNumber: Int, failedRequest: Int){
    var count2 = count
    count2++
    if(requestNumber > failedRequest){
        if(count2 % failedRequest == 0){
            return println("request can not be sent")
        }
        return println("failed request exceed the number of Request")
    }
    return //TODO: send request
}