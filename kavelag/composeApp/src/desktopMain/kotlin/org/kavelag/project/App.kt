package org.kavelag.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit

import kavelag.composeapp.generated.resources.Res
import kavelag.composeapp.generated.resources.compose_multiplatform
import kavelag.composeapp.generated.resources.logo
import org.jetbrains.skia.impl.Stats.enabled
@Composable
private fun FunctionBox(name : String){
    val expandedState = remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 10.dp, end = 10.dp)
            .clickable {
                // Lors d'un clic, on inverse l'état de la boîte
                expandedState.value = !expandedState.value
            }
    ) {
        // Boîte avec texte et flèche
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
                .height(40.dp) // Ajuste la hauteur
                .background(Color.White, shape = RoundedCornerShape(8.dp))
                .border(0.5.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                .padding(start = 16.dp, end = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Texte centré à gauche
                Text(
                    text = name,
                    fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                )

                // Flèche qui change de direction
                Icon(
                    imageVector = if (expandedState.value) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Arrow",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
    if(expandedState.value) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp)
                .height(60.dp) // Ajuste la hauteur
                .background(Color.White)
                .border(0.5.dp, Color.Gray, shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically // Centre verticalement les éléments du Row
            ) {
                Text(
                    text = "Params",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(end = 10.dp, bottom = 2.dp) // Espace à droite de "Url"
                )

                CustomTextField(
                    leadingIcon = null,
                    trailingIcon = null,
                    modifier = Modifier
                        .background(
                            MaterialTheme.colors.surface,
                        )
                        .border(0.5.dp, Color.Gray, shape = RoundedCornerShape(3.dp))
                        .fillMaxWidth(0.8f) // Largeur fixe du champ texte
                        .height(22.dp), // Hauteur fixe pour un bon rendu
                    fontSize = 15.sp,
                    placeholderText = ""
                )
            }

        }
    }
}
@Composable
private fun CustomTextField(
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    placeholderText: String = "Placeholder",
    fontSize: TextUnit = MaterialTheme.typography.body2.fontSize
) {
    var text by rememberSaveable { mutableStateOf("") }
    BasicTextField(
        modifier = modifier
            .background(
                MaterialTheme.colors.surface,
                MaterialTheme.shapes.small,
            )
            .fillMaxWidth(),
        value = text,
        onValueChange = {
            text = it
        },
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colors.primary),
        textStyle = LocalTextStyle.current.copy(
            color = MaterialTheme.colors.onSurface,
            fontSize = fontSize
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) leadingIcon()

                Box(Modifier.weight(1f)) {
                    if (text.isEmpty()) {
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
@Preview
fun App() {
    var text by remember { mutableStateOf("") }


    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
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

                verticalAlignment = Alignment.CenterVertically // Alignement vertical de l'image et du texte
            ) {
                // Image à gauche
                Image(
                    painter = painterResource(Res.drawable.logo), // Référence à votre image
                    contentDescription = "Une image exemple",
                    modifier = Modifier
                        .size(40.dp) // Ajuster la taille de l'image
                        .padding(end = 8.dp) // Espace entre l'image et le texte
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
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
                        .background(Color(0xFFF8F8F8))
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally // Aligne tous les enfants de la colonne horizontalement
                ) {
                    // Texte Host
                    Text(
                        text = "Host",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp, top = 10.dp) // Ajoute un espace en dessous
                    )

                    // Ligne contenant "Url" et le champ texte
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(), // Remplir toute la largeur disponible
                        contentAlignment = Alignment.Center // Centrer le contenu à l'intérieur de la Box
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically // Centre verticalement les éléments du Row
                        ) {
                            Text(
                                text = "Url",
                                fontSize = 15.sp,
                                modifier = Modifier.padding(end = 10.dp, bottom = 2.dp) // Espace à droite de "Url"
                            )

                            CustomTextField(
                                leadingIcon = null,
                                trailingIcon = null,
                                modifier = Modifier
                                    .background(
                                        MaterialTheme.colors.surface,
                                    )
                                    .border(0.5.dp, Color.Gray, shape = RoundedCornerShape(3.dp))
                                    .fillMaxWidth(0.8f) // Largeur fixe du champ texte
                                    .height(22.dp), // Hauteur fixe pour un bon rendu
                                fontSize = 10.sp,
                                placeholderText = "..."
                            )
                        }
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

                    FunctionBox("Latency")
                    FunctionBox("Package Loss")
                    FunctionBox("Network Error")
                }
            }
        }
    }
}