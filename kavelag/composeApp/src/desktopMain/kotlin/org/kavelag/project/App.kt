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
import kavelag.composeapp.generated.resources.Res
import kavelag.composeapp.generated.resources.logo
import kotlinx.coroutines.*
import org.jetbrains.compose.resources.painterResource


@Composable
@Preview
fun App() {
    val clientScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    var Url by remember { mutableStateOf("") }
    val portValues = remember { mutableStateListOf<String>().apply { repeat(1) { add("") } } }
    var LatencyParam by remember { mutableStateOf("") }
    var PackageLossEnabled by remember { mutableStateOf(false) }
    var NetworkErrorEnabled by remember { mutableStateOf(false) }
    var number by remember { mutableStateOf(1) }
    var showPortLengthError by remember { mutableStateOf(false) }
    var showSendError by remember { mutableStateOf(false) }
    var isProxyRunning by remember { mutableStateOf(false) }
    var FunctionAlreadySelected by remember { mutableStateOf("") }
    var showPopUp by remember { mutableStateOf(false) }
    if (showPortLengthError) {
        LaunchedEffect(Unit) {
            delay(3000) // Attendre 3 secondes
            showPortLengthError = false // Masquer le message après le délai
        }
    }

    if (showSendError) {
        LaunchedEffect(Unit) {
            delay(3000) // Attendre 3 secondes
            showSendError = false // Masquer le message après le délai
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
            horizontalAlignment = Alignment.Start // Aligner la colonne à gauche
        ) {
            // Row pour afficher l'image et le texte à gauche
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(Color.DarkGray),

                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(Res.drawable.logo),
                    contentDescription = "Une image exemple",
                    modifier = Modifier
                        .size(40.dp) // Ajuster la taille de l'image
                        .padding(end = 8.dp) // Espace entre l'image et le texte
                )
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .size(20.dp) // Taille personnalisée du bouton
                        .background(Color.DarkGray, shape = RoundedCornerShape(4.dp))
                        .clickable(onClick = { showPopUp = true }),
                    contentAlignment = Alignment.Center // Centre l'icône dans la Box
                ) {
                    Icon(
                        imageVector = Question_mark,
                        contentDescription = "Question Mark Icon",
                        tint = Color.White // vous pouvez maintenant passer une couleur personnalisée ici
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
                        .fillMaxWidth(0.55f) // Largeur fixe de 400 dp
                        .fillMaxHeight()
                        .padding(10.dp),
                    horizontalAlignment = Alignment.Start // Aligner la colonne à gauche
                ) {            // Vous pouvez ajouter d'autres éléments à la colonne
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .border(0.5.dp, Color.Gray, shape = RoundedCornerShape(5.dp))
                    ) {
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
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .border(0.5.dp, Color.Gray, shape = RoundedCornerShape(5.dp))
                    ) {
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
                    }
                }
                Column(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(1f)
                        .background(Color(0xFFF8F8F8))
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally // Aligne tous les enfants de la colonne horizontalement
                ) {
                    Text(
                        text = "Host",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp, top = 10.dp) // Ajoute un espace en dessous
                    )
                    Column(
                        Modifier
                            .padding(start = 10.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.Start // Aligne tous les enfants de la colonne horizontalement
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(), // Remplir toute la largeur disponible
                            // Centrer le contenu à l'intérieur de la Box
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically // Centre verticalement les éléments du Row
                            ) {
                                Text(
                                    text = "Url",
                                    fontSize = 15.sp,
                                    modifier = Modifier
                                        .padding(end = 10.dp, bottom = 2.dp)
                                        .fillMaxWidth(0.1f)

                                )
                                CustomTextField(
                                    value = Url, // Passe l'état actuel
                                    onValueChange = { newValue ->
                                        Url = newValue // Met à jour l'état lorsque le texte change
                                    },
                                    enabled = !isProxyRunning,
                                    leadingIcon = null,
                                    trailingIcon = null,
                                    modifier = Modifier
                                        .background(
                                            MaterialTheme.colors.surface,
                                        )
                                        .border(0.5.dp, Color.Gray, shape = RoundedCornerShape(3.dp))
                                        .fillMaxWidth(0.85f) // Largeur fixe du champ texte
                                        .height(22.dp), // Hauteur fixe pour un bon rendu
                                    fontSize = 14.sp,
                                    placeholderText = "..."
                                )

                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically // Centre verticalement les éléments du Row
                        ) {
                            Text(
                                text = "Port",
                                fontSize = 15.sp,
                                modifier = Modifier
                                    .padding(end = 10.dp, bottom = 2.dp)
                                    .fillMaxWidth(0.1f)

                            )
                            repeat(number) { index ->
                                // Commence une nouvelle ligne pour chaque 3 éléments
                                if (index % 3 == 0) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(end = 10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween // Espacement uniforme entre les éléments
                                    ) {
                                        // Ajout des éléments de la ligne
                                        for (i in 0 until 3) {
                                            if (index + i < number) {
                                                CustomTextField(
                                                    value = portValues[index + i], // Passe l'état actuel
                                                    onValueChange = { newValue ->
                                                        portValues[index + i] =
                                                            newValue // Met à jour l'état lorsque le texte change
                                                    },
                                                    enabled = !isProxyRunning,
                                                    leadingIcon = null,
                                                    trailingIcon = null,
                                                    modifier = Modifier
                                                        .background(
                                                            MaterialTheme.colors.surface,
                                                        )
                                                        .border(0.5.dp, Color.Gray, shape = RoundedCornerShape(3.dp))
                                                        .weight(1f) // Largeur proportionnelle // Espacement latéral
                                                        .height(22.dp), // Hauteur fixe pour un bon rendu
                                                    fontSize = 14.sp,
                                                    placeholderText = "..."
                                                )
                                            } else {
                                                Spacer(modifier = Modifier.weight(1f)) // Ajout d'un espace vide si pas assez d'éléments
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(20.dp))
                                        if (number != 1) {
                                            Box(
                                                modifier = Modifier
                                                    .size(20.dp) // Taille personnalisée du bouton
                                                    .background(
                                                        if (!isProxyRunning) {
                                                            Color.DarkGray
                                                        } else {
                                                            Color.Gray
                                                        },
                                                        shape = RoundedCornerShape(4.dp)
                                                    ) // Fond carré
                                                    .clickable {
                                                        if (!isProxyRunning) {
                                                            number--;
                                                            portValues.removeLast();
                                                            if (showPortLengthError == true) {
                                                                showPortLengthError = false
                                                            }
                                                        }
                                                    },

                                                contentAlignment = Alignment.Center // Centre l'icône dans la Box
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.Delete,
                                                    contentDescription = "Delete icon",
                                                    tint = Color.White, // Couleur de l'icône en blanc
                                                    modifier = Modifier.size(12.dp) // Taille de l'icône
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
                            .size(20.dp) // Taille personnalisée du bouton
                            .background(
                                if (!isProxyRunning) {
                                    Color.DarkGray
                                } else {
                                    Color.Gray
                                }, shape = RoundedCornerShape(4.dp)
                            ) // Fond carré
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
                        contentAlignment = Alignment.Center // Centre l'icône dans la Box
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add icon",
                            tint = Color.White, // Couleur de l'icône en blanc
                            modifier = Modifier.size(12.dp) // Taille de l'icône
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
                            .padding(vertical = 25.dp) // Espacement au-dessus et en dessous du Divider
                            .fillMaxWidth(0.5f) // Le Divider prend 80% de la largeur
                            .height(1.dp), // Hauteur du Divider
                        color = Color.Gray // Vous pouvez ajuster la couleur
                    )

                    Text(
                        text = "Function",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp) // Ajoute un espace en dessous
                    )

                    FunctionBox(
                        "Latency",
                        FunctionAlreadySelected,
                        isProxyRunning,
                        value = LatencyParam, // Passe l'état actuel
                        onValueChange = { newValue ->
                            LatencyParam = newValue // Met à jour l'état lorsque le texte change
                        },
                    )
                    FunctionBox(
                        "Package Loss",
                        FunctionAlreadySelected,
                        isProxyRunning,
                        valueBool = PackageLossEnabled, // Passe l'état actuel
                        onValueBoolChange = {
                            PackageLossEnabled = !PackageLossEnabled // Met à jour l'état lorsque le texte change
                        },
                    )
                    FunctionBox("Network Error",
                        FunctionAlreadySelected,
                        isProxyRunning,
                        valueBool = NetworkErrorEnabled, // Passe l'état actuel
                        onValueBoolChange = {
                            NetworkErrorEnabled = !NetworkErrorEnabled
                        })

                    Spacer(modifier = Modifier.height(20.dp)) // Espacement avant le bouton
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
                            backgroundColor = if (isProxyRunning) Color.Red else Color.DarkGray, // Change la couleur selon l'état
                            contentColor = Color.White // Couleur du texte ou du contenu
                        ),
                        onClick = {
                            // Action à exécuter lors du clic sur le bouton
                            clientScope.launch {
                                try {
                                    for (port in portValues) {
                                        if (Url.isNotEmpty() && port.isNotEmpty() && FunctionAlreadySelected.isNotEmpty()) {
                                            println(Url)
                                            println(port)
//                                            val configuration = DestinationServerConfig(Url, port.toInt())
//                                            SetUserConfigurationChannel.destinationServerAddress.send(configuration)
                                            if (LatencyParam.isNotEmpty()) {
                                                println(LatencyParam)
                                            }
                                            if (PackageLossEnabled) {
                                                println(PackageLossEnabled)
                                            }
                                            if (NetworkErrorEnabled) {
                                                println(NetworkErrorEnabled)
                                            }
                                            isProxyRunning = !isProxyRunning
                                        } else {
                                            showSendError = true
                                        }
                                    }
                                } catch (e: Exception) {
                                    println("Error: ${e.message}")
                                }
                            }

                        },
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(0.5f), // Le bouton occupe 50% de la largeur
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (isProxyRunning) "Stop Proxy" else "Start Proxy", // Change le texte selon l'état
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
                    text = "Le Lorem Ipsum est simplement du faux texte employé dans la composition et la mise en page avant impression. Le Lorem Ipsum est le faux texte standard de l'imprimerie depuis les années 1500, quand un imprimeur anonyme assembla ensemble des morceaux de texte pour réaliser un livre spécimen de polices de texte. Il n'a pas fait que survivre cinq siècles, mais s'est aussi adapté à la bureautique informatique, sans que son contenu n'en soit modifié. Il a été popularisé dans les années 1960 grâce à la vente de feuilles Letraset contenant des passages du Lorem Ipsum, et, plus récemment, par son inclusion dans des applications de mise en page de texte, comme Aldus PageMaker.",
                    fontSize = 18.sp,
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
    value: String? = null, // Optional parameter
    onValueChange: ((String) -> Unit)? = null, // Optional parameter
    valueBool: Boolean? = null, // Optional parameter
    onValueBoolChange: ((Boolean) -> Unit)? = null // Optional parameter
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
        // Header with title and arrow icon
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

    // Expanded Content
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
                        value = value, // Use value here
                        onValueChange = onValueChange, // Pass callback
                        enabled = !isProxyRunning && (FunctionAlreadySelected == name || FunctionAlreadySelected == ""),
                        modifier = Modifier
                            .background(MaterialTheme.colors.surface)
                            .border(0.5.dp, Color.Gray, RoundedCornerShape(3.dp))
                            .fillMaxWidth(0.8f)
                            .height(22.dp),
                        fontSize = 15.sp,
                        placeholderText = ""
                    )
                } else if ((name == "Package Loss" || name == "Network Error") && valueBool != null && onValueBoolChange != null) {
                    // Checkbox UI
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
    value: String, // La valeur vient du parent
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
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
        onValueChange = onValueChange,
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colors.primary),
        textStyle = LocalTextStyle.current.copy(
            color = if (enabled) MaterialTheme.colors.onSurface else Color.Gray,
            fontSize = fontSize
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier,
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