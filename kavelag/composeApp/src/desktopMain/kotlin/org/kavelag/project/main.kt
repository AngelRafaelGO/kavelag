package org.kavelag.project

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kavelag.composeapp.generated.resources.Res
import kavelag.composeapp.generated.resources.logo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.jetbrains.compose.resources.painterResource
import org.kavelag.project.app.App

fun main() = application {
    val kavelagScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    Window(
        onCloseRequest = ::exitApplication,
        title = "kavelag",
        icon = painterResource(Res.drawable.logo)
    ) {
        App(kavelagScope)
    }
}
