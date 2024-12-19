package org.kavelag.project.targetServerProcessing

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.netty.util.internal.StringUtil
import org.kavelag.project.httpClient
import org.kavelag.project.models.HttpRequest

suspend fun callTargetServer(destinationURL: String, destinationPort: Int, httpRequest: HttpRequest) : String? {
    when (httpRequest.method) {
        "GET" -> return getMethod(destinationURL, destinationPort, httpRequest)
        "POST" -> return postMethod(destinationURL, destinationPort, httpRequest)
    }
    return null
}

suspend fun getMethod(destinationURL: String, destinationPort: Int, httpRequest: HttpRequest): String? {
    runCatching {
        requestBuilder(destinationURL, destinationPort, httpRequest, HttpMethod.Get)
        val response: String = requestBuilder(destinationURL, destinationPort, httpRequest, HttpMethod.Get).bodyAsText()
        return response
    }.onFailure {
        println("Error while sending GET request: ${it.message}")
    }.getOrNull()
    return null
}

suspend fun postMethod(destinationURL: String, destinationPort: Int, httpRequest: HttpRequest): String? {
    runCatching {
        requestBuilder(destinationURL, destinationPort, httpRequest, HttpMethod.Post)
        val response: String = requestBuilder(destinationURL, destinationPort, httpRequest, HttpMethod.Post).bodyAsText()
        return response
    }.onFailure {
        println("Error while sending POST request: ${it.message}")
    }.getOrNull()
    return null
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