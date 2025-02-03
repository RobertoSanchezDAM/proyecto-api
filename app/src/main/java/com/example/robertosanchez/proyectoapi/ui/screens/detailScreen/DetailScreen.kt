package com.example.robertosanchez.proyectoapi.ui.screens.detailScreen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.robertosanchez.proyectoapi.model.MediaItem
import com.example.robertosanchez.proyectoapi.ui.screens.listaScreen.ListaViewModel
import com.example.robertosanchez.proyectoapi.ui.screens.listaScreen.ListaViewModelFactory

@Composable
fun DetailScreen(mediaId: Int, cantante: String, onBack: () -> Unit, navigateToList: (String) -> Unit) {
    val viewModel: ListaViewModel = viewModel(factory = ListaViewModelFactory(cantante))

    LaunchedEffect(Unit) {
        viewModel.cargarDatos(cantante)
    }

    val lista = viewModel._listaCompleta
    val item = lista.firstOrNull { it.id == mediaId }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E))
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

        item?.let { mediaItem ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(mediaItem.image),
                        contentDescription = "Cover",
                        modifier = Modifier.size(250.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Mostrar detalles de la canción y artista
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.name,
                            fontSize = 24.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = item.artists.joinToString(separator = ", ") { it.name },
                            fontSize = 14.sp,
                            color = Color.White,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "${item.type.replaceFirstChar { it.uppercase() }} · ${item.released_date.year}",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }

                    FloatingActionButton(
                        onClick = { /*  */ },
                        containerColor = Color(0xFF1ED760),
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                    ) {
                        Icon(Icons.Default.PlayArrow,
                            contentDescription = "Reproducir",
                            tint = Color.Black)
                    }
                }

                // Detalles de la canción
                SongDetailRow(mediaItem)

                // Artistas participantes
                ArtistsSection(mediaItem, navigateToList)
            }
        } ?: run {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "Elemento no cargado.",
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
fun SongDetailRow(item: MediaItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .wrapContentHeight()
            .clip(MaterialTheme.shapes.small)
            .background(Color(0xFF0F0F0F)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(8.dp))

        // Column con el nombre de la canción y del artista o los artistas de la cancion
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.name,
                fontSize = 18.sp,
                color = Color.White
            )
            Text(
                text = item.artists.joinToString(separator = ", ") { featuredItem ->
                    featuredItem.name
                },
                color = Color.Gray,
                fontSize = 14.sp
            )
        }

        Icon(
            Icons.Default.MoreVert,
            contentDescription = "Más opciones",
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun ArtistsSection(item: MediaItem, navigateToList: (String) -> Unit) {
    Text(
        text = "Artistas participantes",
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier.padding(bottom = 8.dp)
    )

    item.artists.forEach { featuredArtist ->
        ArtistaItem(imageUrl = featuredArtist.image_url, artistName = featuredArtist.name, navigateToList)
    }
}

@Composable
fun ArtistaItem(imageUrl: String, artistName: String, navigateToList: (String) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable {
                navigateToList(artistName)},
    ) {
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = "Artista",
            modifier = Modifier
                .size(65.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = artistName,
            fontSize = 20.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}
