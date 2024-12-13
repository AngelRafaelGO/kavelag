package org.kavelag.project

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.*
import org.kavelag.project.routing.configureRouting
import org.kavelag.project.socketController.ProxySocketReceiver

fun main() {
    val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    appScope.launch {
        try {
            ProxySocketReceiver.launchProxySocket("proxy is up!")
        } catch (e: Exception) {
            println("Error in SocketProxyReceiver: ${e.message}")
        }
    }

    Runtime.getRuntime().addShutdownHook(Thread {
        println("Shutting down application...")
        appScope.cancel()
    })

    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureRouting()
}