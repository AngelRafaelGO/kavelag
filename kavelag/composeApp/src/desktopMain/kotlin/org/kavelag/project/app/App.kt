package org.kavelag.project.app

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import org.kavelag.project.assets.questionMarkSVG
import org.kavelag.project.components.*
import org.kavelag.project.models.ResponseItem

@Composable
@Preview
fun App(kavelagScope: CoroutineScope) {
    val viewModel = remember { AppViewModel() }
    val scrollStateRequests = rememberScrollState()
    val scrollStateResponses = rememberScrollState()

    LaunchedEffect(viewModel.responses.size) {
        scrollStateResponses.animateScrollTo(scrollStateResponses.maxValue)
    }

    LaunchedEffect(viewModel.requests.size) {
        scrollStateRequests.animateScrollTo(scrollStateRequests.maxValue)
    }

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
                        imageVector = questionMarkSVG,
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
                    popUpHelp(onDismiss = { viewModel.showPopUpHelp = false })
                }
                if (viewModel.showPopUpPref) {
                    popUpPref(viewModel, onDismiss = { viewModel.showPopUpPref = false })
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
                                        .width(60.dp)
                                        .height(18.dp)
                                        .background(
                                            Color.DarkGray, shape = RoundedCornerShape(4.dp)
                                        )
                                        .clickable {
                                            viewModel.clearRequest()
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Clear",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        lineHeight = 10.sp,
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxHeight(0.95f)
                            ) {
                                Text(
                                    text = viewModel.requests.joinToString(separator = "\n"),
                                    color = Color.DarkGray,
                                    fontSize = 10.sp,
                                    fontStyle = FontStyle.Italic,
                                    lineHeight = 12.sp,
                                    modifier = Modifier
                                        .weight(1f)
                                        .verticalScroll(scrollStateRequests)
                                        .padding(start = 10.dp, end = 10.dp),
                                )
                                verticalScrollbar(
                                    modifier = Modifier.fillMaxHeight(0.95f),
                                    scrollState = scrollStateRequests
                                )
                            }
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
                                        lineHeight = 10.sp,
                                    )

                                }
                                Box(
                                    modifier = Modifier
                                        .width(60.dp)
                                        .height(18.dp)
                                        .background(
                                            Color.DarkGray, shape = RoundedCornerShape(4.dp)
                                        )
                                        .clickable {
                                            viewModel.clearResponse()
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Clear",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        lineHeight = 10.sp,
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxHeight(0.95f)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .verticalScroll(scrollStateResponses)
                                ) {
                                    viewModel.responses.forEachIndexed { index, responseItem: ResponseItem ->
                                        Text(
                                            text = responseItem.textValue,
                                            color = Color(responseItem.colorValue ?: 0xFFA9A9A9),
                                            fontSize = 10.sp,
                                            fontStyle = FontStyle.Italic,
                                            lineHeight = 12.sp,
                                            modifier = Modifier
                                                .padding(start = 10.dp, end = 10.dp),
                                        )
                                        Text(
                                            text = "Response time : ${viewModel.timeResponse[index]} ms",
                                            color = Color(responseItem.colorValue ?: 0xFFA9A9A9),
                                            fontSize = 10.sp,
                                            fontStyle = FontStyle.Italic,
                                            lineHeight = 12.sp,
                                            modifier = Modifier
                                                .padding(start = 10.dp, end = 10.dp),
                                        )
                                    }
                                }
                                verticalScrollbar(
                                    modifier = Modifier.fillMaxHeight(0.95f),
                                    scrollState = scrollStateResponses
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
                                customTextField(
                                    value = viewModel.url,
                                    onValueChange = { newValue ->
                                        viewModel.url = newValue
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
                                                customTextField(
                                                    value = viewModel.portValues[index + i],
                                                    onValueChange = { newValue ->
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
                                                        .border(
                                                            0.5.dp,
                                                            Color.Gray,
                                                            shape = RoundedCornerShape(3.dp)
                                                        )
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
                                val url = viewModel.url
                                val ports = viewModel.portValues
                                viewModel.savePreferenceSettings(url, ports)
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
                            text = "Port number limit has been reached",
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
                    functionBox(
                        "Latency",
                        viewModel.functionAlreadySelected,
                        viewModel.isProxyRunning,
                        value = viewModel.latencyParam,
                        onValueChange = { newValue ->
                            viewModel.latencyParam = newValue
                        },
                        secondValue = viewModel.latencyParam2,
                        onSecondValueChange = { newValue ->
                            viewModel.latencyParam2 = newValue
                        },
                    )
                    functionBox(
                        "Random Fail",
                        viewModel.functionAlreadySelected,
                        viewModel.isProxyRunning,
                        valueBool = viewModel.packageLossEnabled,
                        onValueBoolChange = {
                            viewModel.packageLossEnabled = !viewModel.packageLossEnabled
                        },
                    )
                    functionBox(
                        "Network Error",
                        viewModel.functionAlreadySelected,
                        viewModel.isProxyRunning,
                        valueBool = viewModel.networkErrorEnabled,
                        onValueBoolChange = {
                            viewModel.networkErrorEnabled = !viewModel.networkErrorEnabled
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
