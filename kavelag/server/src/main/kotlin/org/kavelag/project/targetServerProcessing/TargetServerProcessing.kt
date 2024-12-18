package org.kavelag.project.targetServerProcessing

import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.kavelag.project.httpClient
import org.kavelag.project.models.HttpRequest

suspend fun callTargetServer(destinationURL: String, destinationPort: Int, httpRequest: HttpRequest) {
    val serverResponse: HttpResponse = httpClient.post("${destinationURL}:${destinationPort}") {
        setBody(httpRequest.body)
    }
    println("\n $serverResponse \n ${serverResponse.bodyAsText()}")
}