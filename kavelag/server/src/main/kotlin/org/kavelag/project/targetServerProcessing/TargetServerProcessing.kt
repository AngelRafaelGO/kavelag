package org.kavelag.project.targetServerProcessing

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.netty.util.internal.StringUtil
import org.kavelag.project.httpClient
import org.kavelag.project.models.HttpRequest

suspend fun callTargetServer(destinationURL: String, destinationPort: Int, httpRequest: HttpRequest) {
    when (httpRequest.method) {
        "GET" -> getMethod(destinationURL, destinationPort, httpRequest)
        "POST" -> postMethod(destinationURL, destinationPort, httpRequest)
    }

}

suspend fun getMethod(destinationURL: String, destinationPort: Int, httpRequest: HttpRequest) {
    runCatching {
        requestBuilder(destinationURL, destinationPort, httpRequest, HttpMethod.Get)
    }.onFailure {
        println("Error while sending POST request: ${it.message}")
    }.getOrNull()
}

suspend fun postMethod(destinationURL: String, destinationPort: Int, httpRequest: HttpRequest) {
    runCatching {
        requestBuilder(destinationURL, destinationPort, httpRequest, HttpMethod.Post)
    }.onFailure {
        println("Error while sending POST request: ${it.message}")
    }.getOrNull()
}

suspend fun requestBuilder(
    destinationURL: String,
    destinationPort: Int,
    httpRequest: HttpRequest,
    methodRequested: HttpMethod
): HttpResponse {
    val headers = httpRequest.headers
    val body = httpRequest.body

    return httpClient.request("${destinationURL}:${destinationPort}${httpRequest.requestedResource}") {
        method = methodRequested
        if (headers.isNotEmpty())
            headers {
                headers.forEach { (key, value) ->
                    append(key, value)
                }
            }
        if (body != null) {
            setBody(body)
        }
    }
}