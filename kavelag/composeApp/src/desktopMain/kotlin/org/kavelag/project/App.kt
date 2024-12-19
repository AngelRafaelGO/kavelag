package org.kavelag.project

import Question_mark
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import kotlinx.coroutines.*
import org.kavelag.project.models.AppliedNetworkAction
import org.kavelag.project.models.ProxySocketConfiguration


@Composable
@Preview
fun App(appScope: CoroutineScope) {
    var Url by remember { mutableStateOf("") }
    val portValues = remember { mutableStateListOf<String>().apply { repeat(1) { add("") } } }
    var LatencyParam by remember { mutableStateOf("") }
    var PackageLossEnabled by remember { mutableStateOf(false) }
    var NetworkErrorEnabled by remember { mutableStateOf(false) }


    val clientScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    var number by remember { mutableStateOf(1) }
    var isProxyRunning by remember { mutableStateOf(false) }
    var FunctionAlreadySelected by remember { mutableStateOf("") }

    var showPortLengthError by remember { mutableStateOf(false) }
    var showSendError by remember { mutableStateOf(false) }
    var showPopUp by remember { mutableStateOf(false) }

    val requests = remember { mutableStateListOf<String>() }
    val responses = remember { mutableStateListOf<String>() }

    suspend fun listenerForRequests() {
        for (request in SetUserConfigurationChannel.incomingHttpData) {
            requests.add(request.httpIncomingData)
        }
    }

    suspend fun listenerForResponses() {
        for (response in SetUserConfigurationChannel.destinationServerResponseData) {
            println("-------------------------------------------")
            println(response)
            responses.add(response.httpDestinationServerResponse)
//            response.httpDestinationServerResponse?.let { responses.add(it) }
        }
    }



    if (showPortLengthError) {
        LaunchedEffect(Unit) {
            delay(3000)
            showPortLengthError = false
        }
    }
    if (showSendError) {
        LaunchedEffect(Unit) {
            delay(3000)
            showSendError = false
        }
    }
    LaunchedEffect(LatencyParam) {
        if (LatencyParam.isNotEmpty()) FunctionAlreadySelected = "Latency" else FunctionAlreadySelected = ""
    }
    LaunchedEffect(PackageLossEnabled) {
        if (PackageLossEnabled) FunctionAlreadySelected = "Package Loss" else FunctionAlreadySelected = ""
    }
    LaunchedEffect(NetworkErrorEnabled) {
        if (NetworkErrorEnabled) FunctionAlreadySelected = "Network Error" else FunctionAlreadySelected = ""
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
                        .clickable(onClick = { showPopUp = true }),
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
                if (showPopUp) {
                    PopUpHelp(onDismiss = { showPopUp = false })
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
                                Box(
                                    modifier = Modifier
                                        .width(40.dp)
                                        .height(18.dp)
                                        .background(Color.Green),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Get",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        lineHeight = 12.sp,
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = requests.joinToString(separator = "\n"),
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
                                Box(
                                    modifier = Modifier
                                        .width(40.dp)
                                        .height(18.dp)
                                        .background(Color.Green),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Get",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        lineHeight = 12.sp,
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            for (response in responses) {
                                Text(
                                    text = response,
                                    color = Color.DarkGray,
                                    fontSize = 10.sp,
                                    fontStyle = FontStyle.Italic,
                                    lineHeight = 12.sp,
                                    modifier = Modifier
                                        .fillMaxHeight(0.05f)
                                        .verticalScroll(rememberScrollState())
                                        .padding(start = 10.dp, end = 10.dp),
                                )
                            }
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
                                    value = Url,
                                    onValueChange = { newValue ->
                                        Url = newValue
                                    },
                                    enabled = !isProxyRunning,
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
                            repeat(number) { index ->
                                if (index % 3 == 0) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(end = 10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        for (i in 0 until 3) {
                                            if (index + i < number) {
                                                CustomTextField(
                                                    value = portValues[index + i],
                                                    onValueChange = { newValue ->
                                                        portValues[index + i] =
                                                            newValue
                                                    },
                                                    enabled = !isProxyRunning,
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
                                        if (number != 1) {
                                            Box(
                                                modifier = Modifier
                                                    .size(20.dp)
                                                    .background(
                                                        if (!isProxyRunning) {
                                                            Color.DarkGray
                                                        } else {
                                                            Color.Gray
                                                        },
                                                        shape = RoundedCornerShape(4.dp)
                                                    )
                                                    .clickable {
                                                        if (!isProxyRunning) {
                                                            number--;
                                                            portValues.removeLast();
                                                            if (showPortLengthError == true) {
                                                                showPortLengthError = false
                                                            }
                                                        }
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
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(
                                if (!isProxyRunning) {
                                    Color.DarkGray
                                } else {
                                    Color.Gray
                                }, shape = RoundedCornerShape(4.dp)
                            )
                            .clickable {
                                if (!isProxyRunning) {
                                    if (number < 3) {
                                        portValues.add("")
                                        number++
                                    } else {
                                        showPortLengthError = true
                                    }
                                }
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
                    if (showPortLengthError) {
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
                        FunctionAlreadySelected,
                        isProxyRunning,
                        value = LatencyParam,
                        onValueChange = { newValue ->
                            LatencyParam = newValue
                        },
                    )
                    FunctionBox(
                        "Package Loss",
                        FunctionAlreadySelected,
                        isProxyRunning,
                        valueBool = PackageLossEnabled,
                        onValueBoolChange = {
                            PackageLossEnabled = !PackageLossEnabled
                        },
                    )
                    FunctionBox("Network Error",
                        FunctionAlreadySelected,
                        isProxyRunning,
                        valueBool = NetworkErrorEnabled,
                        onValueBoolChange = {
                            NetworkErrorEnabled = !NetworkErrorEnabled
                        })

                    Spacer(modifier = Modifier.height(20.dp))
                    if (showSendError) {
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
                            backgroundColor = if (isProxyRunning) Color.Red else Color.DarkGray,
                            contentColor = Color.White
                        ),
                        onClick = {
                            if (!isProxyRunning) {
                                if (Url.isNotEmpty() && portValues.isNotEmpty() && FunctionAlreadySelected.isNotEmpty()) {
                                    if (portValues.all { it.isNotEmpty() }) {
                                        clientScope.launch {
                                            try {
                                                if (LatencyParam.isNotEmpty()) {
                                                    val proxySocketConfiguration = ProxySocketConfiguration(
                                                        Url,
                                                        portValues[0].toInt(),
                                                        AppliedNetworkAction("latency", LatencyParam.toInt())
                                                    )
                                                    appScope.launch {
                                                        startServer(proxySocketConfiguration)
                                                    }
                                                }
                                                isProxyRunning = !isProxyRunning
                                                listenerForRequests()
                                                listenerForResponses()
                                                if (PackageLossEnabled) {
                                                    println(PackageLossEnabled)
                                                }
                                                if (NetworkErrorEnabled) {
                                                    println(NetworkErrorEnabled)
                                                }

                                            } catch (e: Exception) {
                                                println("Error: ${e.message}")
                                            }
                                        }
                                    } else {
                                        showSendError = true
                                    }
                                } else {
                                    showSendError = true
                                }
                            } else {
                                isProxyRunning = !isProxyRunning
                                runBlocking {
                                    stopServer()
                                }
                            }
                        },
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(0.5f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (isProxyRunning) "Stop Proxy" else "Start Proxy",
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
                    text = "Connect your application (Client) to our proxy by making a request to the following address: \"http://localhost:8080/\"\n" +
                            "\n" +
                            "Next, fill in the following fields on the Kavelag interface: URL, Port, and the desired functionality.\n" +
                            "\n" +
                            "There are several types of functionalities:\n" +
                            "\n" +
                            "- Latency: Simulates network latency on the path of your request.\n" +
                            "- Packet Loss: Simulates packet loss during the reception of the request.\n" +
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
                } else if ((name == "Package Loss" || name == "Network Error") && valueBool != null && onValueBoolChange != null) {
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