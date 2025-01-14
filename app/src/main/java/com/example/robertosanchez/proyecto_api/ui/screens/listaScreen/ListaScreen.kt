package com.example.robertosanchez.proyecto_api.ui.screens.listaScreen


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.rememberAsyncImagePainter
import com.example.robertosanchez.proyecto_api.model.MediaItem

@Composable
fun ListaScreen(cantante: String, viewModel: ListaViewModel, onBack: () -> Unit, navigateToDetail: (Int, String) -> Unit) {
    val lista by viewModel.listaPag.observeAsState(emptyList())
    val progressBar by viewModel.progressBar.observeAsState(false)

    val cantanteNombre = lista
        .flatMap { it.artists }
        .firstOrNull { it.name.equals(cantante, ignoreCase = true) }?.name

    val cantanteImagen = lista
        .flatMap { it.artists }
        .firstOrNull { it.name.equals(cantante, ignoreCase = true) }?.image_url

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E))
    ) {
        // Botón siempre visible en la esquina superior izquierda
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

        if (progressBar) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Green)
            }
        } else if (cantanteImagen != null) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(model = cantanteImagen),
                            contentDescription = "Imagen de Fondo",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        // Gradiente negro
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Black.copy(alpha = 0.5f), // Negro con transparencia
                                            Color.Transparent // Transparente
                                        ),
                                        startY = 1000f,
                                        endY = 0f // Ajusta el valor para controlar la altura del gradiente
                                    )
                                )
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .align(Alignment.BottomStart),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Column {
                                if (cantanteNombre != null) {
                                    Text(
                                        text = cantanteNombre,
                                        style = MaterialTheme.typography.displayLarge.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            shadow = Shadow(
                                                color = Color.Black,
                                                offset = Offset(4f, 4f),
                                                blurRadius = 8f
                                            )
                                        )
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Canciones populares",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 1.sp
                        ),
                        modifier = Modifier.padding(start = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(lista) { mediaItem ->
                    MediaListItem(cantante, mediaItem, navigateToDetail)
                }

                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        if (viewModel.completa()) {
                            TextButton(
                                onClick = { viewModel.cargarMenos() },
                                shape = RectangleShape,
                                elevation = ButtonDefaults.buttonElevation (
                                    defaultElevation = 0.dp
                                )
                            ) {
                                Text(
                                    text = "Ver menos",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            TextButton(
                                onClick = { viewModel.cargarMas() },
                                shape = RectangleShape,
                                elevation = ButtonDefaults.buttonElevation (
                                    pressedElevation = 0.dp
                                )

                            ) {
                                Text(
                                    text = "Ver más",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp)) // Espaciado opcional
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "Artista no encontrado.",
                        color = Color.Gray,
                        fontSize = 18.sp
                    )

                    Text(
                        text = "Vuelva a intentarlo.",
                        color = Color.Gray,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}


@Composable
private fun MediaListItem(cantante: String, mediaItem: MediaItem, navigateToDetail: (Int, String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 16.dp)
            .height(65.dp)
            .clip(MaterialTheme.shapes.small)
            .background(Color(0xFF0F0F0F))
            .clickable { navigateToDetail(mediaItem.id, cantante) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(8.dp))

        Image(
            painter = rememberAsyncImagePainter(mediaItem.image),
            contentDescription = "Imagen de la canción",
            modifier = Modifier
                .size(50.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = mediaItem.name,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                overflow = TextOverflow.Ellipsis
            )

            Row {
                Text(
                    text = mediaItem.artists.joinToString(separator = ", ") { featuredItem ->
                        featuredItem.name
                    },
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "Más opciones",
            tint = Color.Gray,
            modifier = Modifier
                .size(24.dp)
                .clickable {  }
        )

    }
}




