package org.kavelag.project

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kavelag.composeapp.generated.resources.Res
import kavelag.composeapp.generated.resources.logo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

fun main() = application {
    val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    appScope.launch {
        startServer()
    }

    Window(
        onCloseRequest = ::exitApplication,
//        undecorated = true,
        title = "kavelag",
        icon = painterResource(Res.drawable.logo)
    ) {
        App()
    }
}
