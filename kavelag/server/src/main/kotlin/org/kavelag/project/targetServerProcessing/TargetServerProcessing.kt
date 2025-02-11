package org.kavelag.project.targetServerProcessing

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okio.Timeout
import org.kavelag.project.HttpIncomingData
import org.kavelag.project.SetUserConfigurationChannel.incomingHttpData
import org.kavelag.project.httpClient
import org.kavelag.project.models.HttpRequest
import java.net.InetSocketAddress
import java.net.Socket

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
    val selectorManager = ActorSelectorManager(Dispatchers.IO)
    try {
        val url = host.replace(Regex("^https?://"), "").split("/")[0]
        println("🔍 Test de connexion avec ktor-network à $host:$port")
        aSocket(selectorManager).tcp().connect(
            hostname = url,
            port = port
        ).use {
            println("✅ Port $port ouvert sur $host")
        }
        return true
    } catch (e: Exception) {
            println("❌ Port $port fermé ou inaccessible sur $host -> $e")
    return false
    }
}