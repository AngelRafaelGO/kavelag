package org.kavelag.project.targetServerProcessing

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.Dispatchers
import org.kavelag.project.httpClient
import org.kavelag.project.models.HttpRequest

suspend fun callTargetServer(destinationURL: String, destinationPort: Int, httpRequest: HttpRequest): String? {
    when (httpRequest.method) {
        "GET" -> return getMethod(destinationURL, destinationPort, httpRequest)
        "POST" -> return postMethod(destinationURL, destinationPort, httpRequest)
    }
    return null
}

suspend fun getMethod(destinationURL: String, destinationPort: Int, httpRequest: HttpRequest): String? {
    return runCatching {
        val response: String = requestBuilder(destinationURL, destinationPort, httpRequest, HttpMethod.Get).bodyAsText()
        return response
    }.onFailure {
        println("Error while sending GET request: ${it.message}")
    }.getOrNull()
}

suspend fun postMethod(destinationURL: String, destinationPort: Int, httpRequest: HttpRequest): String? {
    return runCatching {
        val response: String =
            requestBuilder(destinationURL, destinationPort, httpRequest, HttpMethod.Post).bodyAsText()
        return response
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
                httpRequest.headers.toString()
            }
        if (httpRequest.method != "GET" && body != null)
            setBody(body)
    }
}

suspend fun isPortOpen(host: String, port: Int): Boolean {
    return try {
        aSocket(ActorSelectorManager(Dispatchers.IO)).tcp().connect(
            hostname = host.replace(Regex("^https?://"), "").split("/")[0],
            port = port
        ).use {
            println("Port $port ouvert sur $host")
            true
        }
    } catch (e: Exception) {
        false
    }
}