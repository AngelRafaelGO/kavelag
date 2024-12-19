package org.kavelag.project

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.*
import org.kavelag.project.models.ProxySocketConfiguration
import org.kavelag.project.plugins.configureSerialization
import org.kavelag.project.socketController.KavelagProxyMainSocket

val httpClient = HttpClient(CIO)
var kavelagScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
private var serverInstance: EmbeddedServer<NettyApplicationEngine, NettyApplicationEngine.Configuration>? = null

fun startServer(proxySocketConfiguration: ProxySocketConfiguration) {
    if (kavelagScope.coroutineContext[Job]?.isCompleted == true) {
        kavelagScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    kavelagScope.launch {
        println("Received user configuration for proxy socket startup: URL=${proxySocketConfiguration.url}, Port=${proxySocketConfiguration.port}")
        try {
            KavelagProxyMainSocket.launchProxySocket(proxySocketConfiguration)
        } catch (e: Exception) {
            println("Error in Kavelag proxy main socket: $e")
        }
    }

    if (serverInstance == null) {
        val server = embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = { module() })
        server.start(wait = false)
        serverInstance = server
    } else {
        println("Kavelag server is already running.")
    }
}

suspend fun stopServer() {
    try {
        KavelagProxyMainSocket.stopProxySocket()
    } catch (e: Exception) {
        println("Error while stopping proxy socket: $e")
    }

    serverInstance?.let {
        it.stop(1000, 2000)
        serverInstance = null
    }

    kavelagScope.cancel()
    println("Kavelag server has been stopped.")
}

fun Application.module() {
    configureSerialization()
}
