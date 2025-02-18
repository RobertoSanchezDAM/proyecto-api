package com.example.robertosanchez.proyectoapi.ui.screens.buscarScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.robertosanchez.proyectoapi.R

@Composable
fun BuscarScreen(onNavigateToLista: (String) -> Unit, onBack: () -> Unit) {
    var text by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E)),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .zIndex(1f)
        ) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = "Atrás",
                tint = Color.White
            )
        }

        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.pxfuel),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.8f))
        ) {
            Text(
                text = "Introduce un artista a buscar:",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = "(Por ejemplo: Michael Jackson)",
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Ingresa el nombre del cantante") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón "Buscar"
            Button(
                onClick = { onNavigateToLista(text) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1ED760)
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Buscar",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

        }
    }
}