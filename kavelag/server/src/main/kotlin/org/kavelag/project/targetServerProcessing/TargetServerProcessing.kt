package org.kavelag.project.targetServerProcessing

import io.ktor.client.request.*
import org.kavelag.project.httpClient
import org.kavelag.project.models.HttpRequest

suspend fun callTargetServer(destinationURL: String, destinationPort: Int, httpRequest: HttpRequest) {
    val serverResponse = httpClient.get("${destinationURL}:${destinationPort}")
}