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

suspend fun getMethod(destinationURL: String, destinationPort: Int, httpRequest: HttpRequest): HttpResponse {
    val headers = httpRequest.headers
    if (headers.isNotEmpty()) {
        println("callTargetServer: method GET: ${httpRequest.headers}")
        return httpClient.get("${destinationURL}:${destinationPort}${httpRequest.requestedResource}") {
            headers {
                httpRequest.headers.forEach { (key, value) ->
                    append(key, value)
                }
            }
        }
    }
    return httpClient.get("${destinationURL}:${destinationPort}${httpRequest.requestedResource}")
}

suspend fun postMethod(destinationURL: String, destinationPort: Int, httpRequest: HttpRequest): HttpResponse {
    runCatching {
        val headers = httpRequest.headers
        val body = httpRequest.body
        if (headers.isNotEmpty()) {
            println("callTargetServer: method POST: ${httpRequest.body}")
            return httpClient.get("${destinationURL}:${destinationPort}${httpRequest.requestedResource}") {
                headers {
                    httpRequest.headers.forEach { (key, value) ->
                        append(key, value)
                    }
                    setBody(body)
                }
            }
        }
    }.onFailure {
        println("Error while sending POST request: ${it.message}")
    }.getOrNull()
    return httpClient.get("${destinationURL}:${destinationPort}${httpRequest.requestedResource}") {
        setBody(body)
    }
}