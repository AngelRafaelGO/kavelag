package org.kavelag.project

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog


@Composable
fun PopUpHelp(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(16.dp),
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "User Manual for Kavelag:",
                    fontSize = 18.sp,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = "Connect your application (Client) to our proxy by making a request to the following address: \"http://localhost:9002/\"\n" +
                            "\n" +
                            "Next, fill in the following fields on the Kavelag interface: URL, Port, and the desired functionality.\n" +
                            "\n" +
                            "There are several types of functionalities:\n" +
                            "\n" +
                            "- Latency: Simulates network latency on the path of your request.\n" +
                            "- Random Fail: Simulates random requests failling.\n" +
                            "- Network Error: Simulates total network loss during the return path of the request.",
                    fontSize = 13.sp,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    }
}

@Composable
fun FunctionBox(
    name: String,
    FunctionAlreadySelected: String,
    isProxyRunning: Boolean,
    value: String? = null,
    onValueChange: ((String) -> Unit)? = null,
    valueBool: Boolean? = null,
    onValueBoolChange: ((Boolean) -> Unit)? = null
) {
    val expandedState = remember { mutableStateOf(false) }
    LaunchedEffect(FunctionAlreadySelected) {
        if (FunctionAlreadySelected != name && FunctionAlreadySelected != "")
            expandedState.value = false
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 10.dp, end = 10.dp)
            .clickable {
                if (FunctionAlreadySelected == name || FunctionAlreadySelected == "")
                    expandedState.value = !expandedState.value
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(
                    if (!isProxyRunning && (FunctionAlreadySelected == name || FunctionAlreadySelected == "")) Color.White else Color.Gray,
                    shape = RoundedCornerShape(8.dp)
                )
                .border(0.5.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = if (expandedState.value) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Arrow",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
    if (expandedState.value) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp)
                .background(Color.White)
                .border(0.5.dp, Color.Gray, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (name == "Latency" && value != null && onValueChange != null) {
                    Text(
                        text = "Params",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(end = 10.dp)
                    )

                    CustomTextField(
                        value = value,
                        onValueChange = onValueChange,
                        enabled = !isProxyRunning && (FunctionAlreadySelected == name || FunctionAlreadySelected == ""),
                        typeNumber = true,
                        modifier = Modifier
                            .background(MaterialTheme.colors.surface)
                            .border(0.5.dp, Color.Gray, RoundedCornerShape(3.dp))
                            .fillMaxWidth(0.8f)
                            .height(22.dp),
                        fontSize = 15.sp,
                        placeholderText = ""
                    )
                } else if ((name == "Random Fail" || name == "Network Error") && valueBool != null && onValueBoolChange != null) {
                    Checkbox(
                        checked = valueBool,
                        onCheckedChange = onValueBoolChange,
                        enabled = !isProxyRunning && (FunctionAlreadySelected == name || FunctionAlreadySelected == ""),
                        modifier = Modifier
                            .height(22.dp),
                    )

                    Text(
                        text = if (valueBool) "Enabled" else "Disabled",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    typeNumber: Boolean,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    placeholderText: String = "Placeholder",
    fontSize: TextUnit = MaterialTheme.typography.body2.fontSize
) {
    BasicTextField(
        modifier = modifier
            .background(
                MaterialTheme.colors.surface,
                MaterialTheme.shapes.small,
            )
            .fillMaxWidth(),
        enabled = enabled,
        value = value,
        onValueChange = { input ->
            if (!typeNumber || input.all { it.isDigit() }) {
                onValueChange(input)
            }
        },
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colors.primary),
        textStyle = LocalTextStyle.current.copy(
            color = if (enabled) MaterialTheme.colors.onSurface else Color.Gray,
            fontSize = fontSize
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .padding(start = 5.dp),
                verticalAlignment = Alignment.CenterVertically

            ) {
                if (leadingIcon != null) leadingIcon()

                Box(Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholderText,
                            style = LocalTextStyle.current.copy(
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f),
                                fontSize = fontSize
                            )
                        )
                    }
                    innerTextField()
                }
                if (trailingIcon != null) trailingIcon()
            }
        }
    )
}

@Composable
fun VerticalScrollbar(
    modifier: Modifier = Modifier,
    scrollState: ScrollState
) {
    val barHeight = 10.dp
    val barPosition = (scrollState.value.toFloat() / scrollState.maxValue.toFloat()).coerceIn(0f, 1f)

    Box(
        modifier = modifier
            .width(6.dp)
            .padding(end = 3.dp)
            .fillMaxHeight()
            .drawBehind {
                drawRoundRect(
                    color = Color.Gray.copy(alpha = 0.6f),
                    topLeft = Offset(0f, size.height * barPosition),
                    size = Size(size.width, barHeight.toPx()),
                    cornerRadius = CornerRadius(5f, 5f)
                )
            }
    )
}