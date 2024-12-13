package org.kavelag.project.targetServerProcessing

import io.ktor.client.request.*
import org.kavelag.project.httpClient
import org.kavelag.project.models.HttpRequest

suspend fun callTargetServer(httpRequest: HttpRequest) {
    val serverResponse = httpClient.get("http://google.com")
}