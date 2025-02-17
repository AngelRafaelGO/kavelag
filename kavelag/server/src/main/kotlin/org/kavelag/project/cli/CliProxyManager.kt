package org.kavelag.project.cli

import org.kavelag.project.models.ProxySocketConfiguration

class CliProxyManager {
    var proxyLaunchConfiguration: ProxySocketConfiguration? = null
    var exitApplication = false
    var isProxyActive = false
    var wasProxyRunning = false

    fun handlePostLaunchInput(postLaunchInput: String) {
        when (postLaunchInput.lowercase()) {
            "quit" -> exitApplication = true
            "stop" -> {
                if (isProxyActive) {
                    isProxyActive = false
                    wasProxyRunning = true
                }
            }

            else -> println("Unknown command. Use 'quit' to exit or 'stop' to deactivate proxy.")
        }
    }
}
