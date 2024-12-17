package org.kavelag.project.network

import kotlinx.coroutines.delay

suspend fun selectHandler(action: String, delay: Long?, count: Int, requestNumber: Int, failedRequest: Int){
    when(action){
        "action 1" -> networkDeconnexion()
        "action 2" -> delay?.let { networkLatency(it) }
        "action 3" -> oneRequestFailsOver2(count, requestNumber, failedRequest)

    }

}

private fun networkDeconnexion(){
    return
    //No request sent
}

private suspend fun networkLatency(delay: Long){
    delay(delay)
    return //TODO: send request
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