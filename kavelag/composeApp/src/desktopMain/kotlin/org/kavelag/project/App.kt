package org.kavelag.project

import Question_mark
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay

@Composable
@Preview
fun App(kavelagScope: CoroutineScope) {

    val viewModel = remember { AppViewModel() }

    if (viewModel.showPortLengthError) {
        LaunchedEffect(Unit) {
            delay(3000)
            viewModel.showPortLengthError = false
        }
    }
    if (viewModel.showSendError) {
        LaunchedEffect(Unit) {
            delay(3000)
            viewModel.showSendError = false
        }
    }

    MaterialTheme {
        Column(
            Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(Color.DarkGray),

                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(Color.DarkGray, shape = RoundedCornerShape(4.dp))
                        .clickable(onClick = { viewModel.showPopUpPref = true }),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Preferences",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(Color.DarkGray, shape = RoundedCornerShape(4.dp))
                        .clickable(onClick = { viewModel.showPopUpHelp = true }),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Question_mark,
                        contentDescription = "Question Mark Icon",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                if (viewModel.showPopUpHelp) {
                    PopUpHelp(onDismiss = { viewModel.showPopUpHelp = false })
                }
                if (viewModel.showPopUpPref) {
                    PopUpPref(viewModel, onDismiss = { viewModel.showPopUpPref = false })
                }
                Column(
                    Modifier
                        .fillMaxWidth(0.55f)
                        .fillMaxHeight()
                        .padding(10.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .border(0.5.dp, Color.Gray, shape = RoundedCornerShape(5.dp))
                    ) {
                        Column() {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(60.dp)
                                        .height(18.dp)
                                        .background(Color.Blue, shape = RoundedCornerShape(topStart = 5.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Request",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        lineHeight = 12.sp,
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = viewModel.requests.joinToString(separator = "\n"),
                                color = Color.DarkGray,
                                fontSize = 10.sp,
                                fontStyle = FontStyle.Italic,
                                lineHeight = 12.sp,
                                modifier = Modifier
                                    .fillMaxHeight(0.95f)
                                    .verticalScroll(rememberScrollState())
                                    .padding(start = 10.dp, end = 10.dp),
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .border(0.5.dp, Color.Gray, shape = RoundedCornerShape(5.dp))
                    ) {
                        Column() {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(60.dp)
                                        .height(18.dp)
                                        .background(Color.Blue, shape = RoundedCornerShape(topStart = 5.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Response",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        lineHeight = 12.sp,
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = viewModel.responses.joinToString(separator = "\n"),
                                color = Color.DarkGray,
                                fontSize = 10.sp,
                                fontStyle = FontStyle.Italic,
                                lineHeight = 12.sp,
                                modifier = Modifier
                                    .fillMaxHeight(0.95f)
                                    .verticalScroll(rememberScrollState())
                                    .padding(start = 10.dp, end = 10.dp),
                            )

                        }
                    }
                }
                Column(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(1f)
                        .background(Color(0xFFF8F8F8))
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Host",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp, top = 10.dp)
                    )
                    Column(
                        Modifier
                            .padding(start = 10.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Url",
                                    fontSize = 15.sp,
                                    modifier = Modifier
                                        .padding(end = 10.dp, bottom = 2.dp)
                                        .fillMaxWidth(0.1f)
                                )
                                CustomTextField(
                                    value = viewModel.Url.value,
                                    onValueChange = { newValue ->
                                        viewModel.updateUrl(newValue)
                                    },
                                    enabled = !viewModel.isProxyRunning,
                                    typeNumber = false,
                                    leadingIcon = null,
                                    trailingIcon = null,
                                    modifier = Modifier
                                        .background(
                                            MaterialTheme.colors.surface,
                                        )
                                        .border(0.5.dp, Color.Gray, shape = RoundedCornerShape(3.dp))
                                        .fillMaxWidth(0.85f)
                                        .height(22.dp),
                                    fontSize = 14.sp,
                                    placeholderText = "..."
                                )

                            }
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Port",
                                fontSize = 15.sp,
                                modifier = Modifier
                                    .padding(end = 10.dp, bottom = 2.dp)
                                    .fillMaxWidth(0.1f)

                            )
                            repeat(viewModel.number) { index ->
                                if (index % 3 == 0) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(end = 10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        for (i in 0 until 3) {
                                            if (index + i < viewModel.number) {
                                                CustomTextField(
                                                    value = viewModel.portValues[index + i],
                                                    onValueChange = { newValue ->
                                                        viewModel.portValues[index + i] = newValue
                                                        viewModel.portValues[index + i] =
                                                            newValue
                                                    },
                                                    enabled = !viewModel.isProxyRunning,
                                                    typeNumber = true,
                                                    leadingIcon = null,
                                                    trailingIcon = null,
                                                    modifier = Modifier
                                                        .background(
                                                            MaterialTheme.colors.surface,
                                                        )
                                                        .border(0.5.dp, Color.Gray, shape = RoundedCornerShape(3.dp))
                                                        .weight(1f)
                                                        .height(22.dp),
                                                    fontSize = 14.sp,
                                                    placeholderText = "..."
                                                )
                                            } else {
                                                Spacer(modifier = Modifier.weight(1f))
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(20.dp))
                                        if (viewModel.number != 1) {
                                            Box(
                                                modifier = Modifier
                                                    .size(20.dp)
                                                    .background(
                                                        if (!viewModel.isProxyRunning) {
                                                            Color.DarkGray
                                                        } else {
                                                            Color.Gray
                                                        },
                                                        shape = RoundedCornerShape(4.dp)
                                                    )
                                                    .clickable {
                                                        viewModel.deletePortSlot()
                                                    },

                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.Delete,
                                                    contentDescription = "Delete icon",
                                                    tint = Color.White,
                                                    modifier = Modifier.size(12.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row {
                        Spacer(modifier = Modifier.weight(1f))
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .background(
                                    if (!viewModel.isProxyRunning) {
                                        Color.DarkGray
                                    } else {
                                        Color.Gray
                                    }, shape = RoundedCornerShape(4.dp)
                                )
                                .clickable {
                                    viewModel.addPortSlot()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Add icon",
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        Button(
                            modifier = Modifier
                                .width(80.dp)
                                .height(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = if (viewModel.isProxyRunning) Color.Red else Color.DarkGray,
                                contentColor = Color.White
                            ),
                            onClick = {
                                viewModel.savePreferenceSettings(viewModel.Url.value, viewModel.portValues)
                            },
                            shape = RoundedCornerShape(4.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(

                                text = "Save params",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .fillMaxHeight()
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                    }
                    if (viewModel.showPortLengthError) {
                        Text(
                            text = "La limite de ports est de 3",
                            color = Color.Red,
                            fontSize = 12.sp,
                            fontStyle = FontStyle.Italic,
                            modifier = Modifier.padding(top = 3.dp)
                        )
                    }
                    Divider(
                        modifier = Modifier
                            .padding(vertical = 25.dp)
                            .fillMaxWidth(0.5f)
                            .height(1.dp),
                        color = Color.Gray
                    )
                    Text(
                        text = "Function",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    FunctionBox(
                        "Latency",
                        viewModel.FunctionAlreadySelected,
                        viewModel.isProxyRunning,
                        value = viewModel.LatencyParam,
                        onValueChange = { newValue ->
                            viewModel.LatencyParam = newValue
                        },
                    )
                    FunctionBox(
                        "Random Fail",
                        viewModel.FunctionAlreadySelected,
                        viewModel.isProxyRunning,
                        valueBool = viewModel.PackageLossEnabled,
                        onValueBoolChange = {
                            viewModel.PackageLossEnabled = !viewModel.PackageLossEnabled
                        },
                    )
                    FunctionBox(
                        "Network Error",
                        viewModel.FunctionAlreadySelected,
                        viewModel.isProxyRunning,
                        valueBool = viewModel.NetworkErrorEnabled,
                        onValueBoolChange = {
                            viewModel.NetworkErrorEnabled = !viewModel.NetworkErrorEnabled
                        })

                    Spacer(modifier = Modifier.height(20.dp))
                    if (viewModel.showSendError) {
                        Text(
                            text = "Error missing arguments check if \"Url\" \"Port\" and \"Function\" are filled ",
                            color = Color.Red,
                            fontSize = 12.sp,
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(bottom = 3.dp)

                        )
                    }
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = if (viewModel.isProxyRunning) Color.Red else Color.DarkGray,
                            contentColor = Color.White
                        ),
                        onClick = {
                            viewModel.toggleProxy(kavelagScope)
                        },
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(0.5f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (viewModel.isProxyRunning) "Stop Proxy" else "Start Proxy",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PopUpHelp(onDismiss: () -> Unit) {
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
private fun PopUpPref(viewModel: AppViewModel, onDismiss: () -> Unit) {
    val selectedItems = remember { mutableStateListOf<PreferenceSettings>() }
    var allSettings by remember { mutableStateOf(viewModel.getAllPreferenceSettings()) }

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
                    text = "Preferences:",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 2.dp)
                )

                Box(modifier = Modifier.weight(1f)) {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(allSettings) { setting ->
                            val isSelected = selectedItems.contains(setting)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        if (isSelected) {
                                            selectedItems.remove(setting)
                                        } else {
                                            selectedItems.add(setting)
                                        }
                                    }
                            ) {
                                Checkbox(
                                    checked = isSelected,
                                    onCheckedChange = {
                                        if (it) selectedItems.add(setting) else selectedItems.remove(setting)
                                    }
                                )
                                Text(
                                    text = setting.toString(),
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            viewModel.removePreferenceSettings(selectedItems)
                            selectedItems.clear()

                            allSettings = viewModel.getAllPreferenceSettings()
                        },
                        enabled = selectedItems.isNotEmpty()
                    ) {
                        Text("Delete")
                    }

                    Button(
                        onClick = {
                            if (selectedItems.isNotEmpty()) {
                                val itemToLoad = selectedItems.first()

                                val loadedData = viewModel.loadPreferenceSettings(itemToLoad)

                                if (loadedData != null) {
                                    viewModel.updateUrl(loadedData.first)
                                    viewModel.updatePorts(loadedData.second)

                                    loadedData.second.forEachIndexed { i, port ->
                                        if (i > 0) {
                                            viewModel.addPortSlot()
                                        }
                                    }
                                    viewModel.updatePorts(loadedData.second)
                                }
                            }
                        },
                        enabled = selectedItems.size == 1
                    ) {
                        Text("Load")
                    }
                }
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