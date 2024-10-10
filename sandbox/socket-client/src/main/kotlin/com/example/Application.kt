package com.example

import com.example.socketClient.SocketClient
import com.example.socketClient.configureSocketClient
import io.ktor.server.application.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        launch {
            val socketClient = SocketClient
            socketClient.launchClient()
        }
    }
}

fun Application.module() {
//    configureRouting()
    configureSocketClient()
}
