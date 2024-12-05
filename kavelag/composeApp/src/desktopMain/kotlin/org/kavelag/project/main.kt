package org.kavelag.project
import androidx.compose.material.Text
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kavelag.composeapp.generated.resources.Res
import kavelag.composeapp.generated.resources.logo
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.dp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import javax.swing.JFrame

fun main() = application {

    Window(
        onCloseRequest = ::exitApplication,
        undecorated = true,
        title = "kavelag",
        icon = painterResource(Res.drawable.logo)
    ) {
        Row(
            modifier = Modifier.background(color = Color(75, 75, 75))
                .fillMaxWidth()
                .height(30.dp)
                .padding(start = 20.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            WindowDraggableArea(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Undecorated window", color = Color.White)
            }
            Row {
                Button(
                    onClick = {
                        val current = AppManager.focusedWindow
                        if (current != null) {
                            current.window.setExtendedState(JFrame.ICONIFIED)
                        }
                    }
                )
                Spacer(modifier = Modifier.width(5.dp))
                Button(
                    onClick = {
                        val current = AppManager.focusedWindow
                        if (current != null) {
                            if (current.window.extendedState == JFrame.MAXIMIZED_BOTH) {
                                current.window.setExtendedState(JFrame.NORMAL)
                            } else {
                                current.window.setExtendedState(JFrame.MAXIMIZED_BOTH)
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.width(5.dp))
                Button(
                    onClick = {
                        AppManager.focusedWindow?.close()
                    }
                )
            }
        }
    }
}

@Composable
fun Button(
    text: String = "",
    onClick: () -> Unit = {},
    color: Color = Color(210, 210, 210),
    size: Int = 16
) {
    val buttonHover = remember { mutableStateOf(false) }
    Surface(
        color = if (buttonHover.value)
            Color(color.red / 1.3f, color.green / 1.3f, color.blue / 1.3f)
        else
            color,
        shape = RoundedCornerShape((size / 2).dp)
    ) {
        Box(
            modifier = Modifier
                .clickable(onClick = onClick)
                .size(size.dp, size.dp)
                .pointerMoveFilter(
                    onEnter = {
                        buttonHover.value = true
                        false
                    },
                    onExit = {
                        buttonHover.value = false
                        false
                    },
                    onMove = { false }
                )
        ) {
            Text(text = text)
        }
    }
}