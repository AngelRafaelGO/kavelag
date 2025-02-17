package org.kavelag.project.cli

import org.kavelag.project.models.AppliedNetworkAction
import org.kavelag.project.models.ProxySocketConfiguration
import org.kavelag.project.startServer
import org.kavelag.project.stopServer

suspend fun main() {
    val cliProxyManager = CliProxyManager()
    cliLauncher(cliProxyManager)
}

suspend fun cliLauncher(cliProxyManager: CliProxyManager) {
    while (!cliProxyManager.exitApplication) {
        if (cliProxyManager.isProxyActive) {
            val postLaunchInput = readlnOrNull()?.trim()

            if (!postLaunchInput.isNullOrBlank()) {
                cliProxyManager.handlePostLaunchInput(postLaunchInput)
            }

            if (!cliProxyManager.isProxyActive && cliProxyManager.wasProxyRunning) {
                stopServer()
            }
        } else {
            getProxyLaunchConfiguration(cliProxyManager)
            cliProxyManager.proxyLaunchConfiguration?.let {
                launchProxy(it)
                cliProxyManager.isProxyActive = true
            } ?: println("Invalid proxy configuration. Please try again.")
        }
    }
}

fun getProxyLaunchConfiguration(cliProxyManager: CliProxyManager) {
    print("Please enter a url: ")
    val urlInput = readlnOrNull()?.trim()

    if (!urlInput.isNullOrBlank()) {
        print("Please enter a port for the above url: ")
        val portInput = readlnOrNull()?.trim()?.toIntOrNull()

        if (portInput != null) {
            cliProxyManager.proxyLaunchConfiguration = ProxySocketConfiguration(
                urlInput,
                intArrayOf(portInput),
                AppliedNetworkAction("latency", 2000, 0)
            )
        } else {
            println("Invalid port number. Please enter a valid integer.")
        }
    }
}

fun launchProxy(proxySocketConfiguration: ProxySocketConfiguration) {
    println(
        """
        Kavelag proxy launched with the following configuration:
        URL  : ${proxySocketConfiguration.url}
        Port : ${proxySocketConfiguration.port}
    """.trimIndent()
    )
    startServer(proxySocketConfiguration)
}