package com.example.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.webjars.*


import io.ktor.http.*
import io.ktor.util.date.*
import kotlinx.coroutines.*

fun Application.configureRouting() {
    install(Webjars) {
        path = "/webjars" //defaults to /webjars
    }
//    install() {
//        info {
//            title = "Example API"
//            version = "latest"
//            description = "Example API for testing and demonstration purposes."
//        }
//        server {
//            url = "http://localhost:8080"
//            description = "Development Server"
//        }
//    }
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        get("/webjars") {
            call.respondText("<script src='/webjars/jquery/jquery.js'></script>", ContentType.Text.Html)
        }
    }
}

fun callServerPoc() {

}