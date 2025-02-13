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
        val response: HttpResponse = requestBuilder(destinationURL, destinationPort, httpRequest, HttpMethod.Get)
        val statusLine = "HTTP/1.1 ${response.status.value} ${response.status.description}\r\n"
        val headers = response.headers.entries().joinToString("\r\n") { "${it.key}: ${it.value.joinToString()}" }
        val body = if (response.status.value != 204 && response.status.value != 304) {
            response.bodyAsText()
        } else {
            ""
        }

        return "$statusLine$headers\r\n\r\n$body"
    }.onFailure {
        println("Error while sending GET request: ${it.message}")
    }.getOrNull()
}

suspend fun postMethod(destinationURL: String, destinationPort: Int, httpRequest: HttpRequest): String? {
    return runCatching {
        val targetServerResponse: HttpResponse =
            requestBuilder(destinationURL, destinationPort, httpRequest, HttpMethod.Post)
        val statusLine = "HTTP/1.1 ${targetServerResponse.status.value} ${targetServerResponse.status.description}\r\n"
        val headers =
            targetServerResponse.headers.entries().joinToString("\r\n") { "${it.key}: ${it.value.joinToString()}" }
        val body = targetServerResponse.bodyAsText()
        return "$statusLine$headers\r\n\r\n$body"
    }.onFailure {
        println("Error while sending POST request to target server: $it")
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

fun isValidUrl(url: String): Boolean {
    val urlRegex: Regex = "^https?://(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}|(?:\\d{1,3}\\.){3}\\d{1,3}$".toRegex()
    return urlRegex.find(url) != null

}