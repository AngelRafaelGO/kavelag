package org.example.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import kavelag_demo.composeapp.generated.resources.Res
import kavelag_demo.composeapp.generated.resources.compose_multiplatform
import org.w3c.dom.Text

@Composable
fun App() {
    var port by remember { mutableStateOf(TextFieldValue("")) }
    var forwardAddress by remember { mutableStateOf(TextFieldValue("")) }
    MaterialTheme {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            TextField(
                value = forwardAddress,
                onValueChange = { newText ->
                    forwardAddress = newText
                }
            )
            Button(onClick = {
                println(port.text)
                println(forwardAddress.text)
            }) {
                Text(text = "Simple Button")
            }
        }
    }
}


//@Composable
//fun SimpleTextField(text: TextFieldValue) {
//    TextField(
//        value = text,
//        onValueChange = { newText ->
//            text = newText
//        }
//    )
//}
//
//@Composable
//fun SimpleButton() {
//    Button(onClick = {
//        println(text)
//    }) {
//        Text(text = "Simple Button")
//    }
//}