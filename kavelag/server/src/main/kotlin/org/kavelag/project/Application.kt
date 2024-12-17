package org.kavelag.project

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.*
import org.kavelag.project.plugins.configureSerialization
import org.kavelag.project.socketController.KavelagProxyMainSocket

val httpClient = HttpClient(CIO)

fun startServer() {
    val serverScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    serverScope.launch {
        listenForConfiguration()
    }
    Runtime.getRuntime().addShutdownHook(Thread {
        println("Shutting down Kavelag proxy server...")
        serverScope.cancel()
    })
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
}

suspend fun listenForConfiguration() {
    for (config in SetUserConfigurationChannel.destinationServerAddress) {
        println("Received user configuration for proxy socket start up: URL=${config.url}, Port=${config.port}")
        try {
            KavelagProxyMainSocket.launchProxySocket(config.url, config.port)
        } catch (e: Exception) {
            println("Error in KavelagProxyMainSocket: ${e.message}")
        }
    }
}