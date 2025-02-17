package com.example.robertosanchez.proyectoapi.ui.screens.usuarioScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.robertosanchez.proyectoapi.R
import com.example.robertosanchez.proyectoapi.data.AuthManager
import com.example.robertosanchez.proyectoapi.db.Song

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuarioScreen(auth: AuthManager, navigateToLogin: () -> Unit, viewModel: UsuarioViewModel) {
    var showDialog by remember { mutableStateOf<DialogType?>(null) }
    var selectedSong by remember { mutableStateOf<Song?>(null) }
    val user = auth.getCurrentUser()
    val uiState by viewModel.uiState.collectAsState()

    val userSongs = uiState.songs.filter { it.userId == user?.uid }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (user?.photoUrl != null) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(user.photoUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Imagen",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .border(1.dp, Color.Black, CircleShape)
                                    .padding(1.dp)
                            )
                        } else {
                            Image(
                                painter = painterResource(R.drawable.profile),
                                contentDescription = "Foto de perfil por defecto",
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, Color.Black, CircleShape)
                                    .padding(1.dp)
                            )
                        }
                        Column {
                            Text(
                                text = user?.displayName ?: "Anónimo",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(
                                text = user?.email ?: "Sin correo",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Black
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1ED760)),
                actions = {
                    IconButton(onClick = { showDialog = DialogType.Logout }) {
                        Icon(
                            Icons.AutoMirrored.Outlined.ExitToApp,
                            contentDescription = "Cerrar sesión",
                            tint = Color.Black
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF1E1E1E))
        ) {
            LazyColumn {
                items(userSongs) { song ->
                    SongItem(
                        song = song,
                        onDelete = { songId -> viewModel.deleteSongById(songId) },
                        onEdit = { song ->
                            selectedSong = song
                            showDialog = DialogType.EditSong
                        }
                    )
                }
            }

            FloatingActionButton(
                onClick = { showDialog = DialogType.AddSong },
                containerColor = Color(0xFF1ED760),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar Canción",
                    tint = Color.Black
                )
            }

            when (showDialog) {
                DialogType.AddSong -> {
                    AddSongDialog(
                        onDismiss = { showDialog = null },
                        onConfirm = { title, anyo ->
                            val newSong = Song(
                                userId = user?.uid ?: "",
                                title = title,
                                anyo = anyo
                            )
                            viewModel.addSong(newSong)
                            showDialog = null
                        }
                    )
                }
                DialogType.EditSong -> {
                    selectedSong?.let { song ->
                        EditSongDialog(
                            song = song,
                            onDismiss = { showDialog = null },
                            onConfirm = { title, anyo ->
                                viewModel.updateSong(song.copy(title = title, anyo = anyo))
                                showDialog = null
                            }
                        )
                    }
                }
                DialogType.Logout -> {
                    LogoutDialog(
                        onDismiss = { showDialog = null },
                        onConfirm = {
                            auth.signOut()
                            navigateToLogin()
                        }
                    )
                }
                else -> Unit
            }
        }
    }
}


// Enum para los tipos de diálogos
enum class DialogType {
    AddSong, Logout, EditSong
}

@Composable
fun AddSongDialog(onDismiss: () -> Unit, onConfirm: (String, Int) -> Unit) {
    var title by remember { mutableStateOf("") }
    var anyo by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1ED760),
        title = { Text("Agregar Canción", color = Color.Black) },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = anyo,
                    onValueChange = { anyo = it },
                    label = { Text("Año de Lanzamiento") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(title, anyo.toIntOrNull() ?: 0) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E1E1E))
            ) {
                Text("Aceptar", color = Color.White)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E1E1E))
            ) {
                Text("Cancelar", color = Color.White)
            }
        }
    )
}

@Composable
fun EditSongDialog(song: Song, onDismiss: () -> Unit, onConfirm: (String, Int) -> Unit) {
    var title by remember { mutableStateOf(song.title ?: "") }
    var anyo by remember { mutableStateOf(song.anyo?.toString() ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1ED760),
        title = { Text("Editar Canción", color = Color.Black) },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = anyo,
                    onValueChange = { anyo = it },
                    label = { Text("Año de Lanzamiento") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(title, anyo.toIntOrNull() ?: 0) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E1E1E))
            ) {
                Text("Guardar", color = Color.White)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E1E1E))
            ) {
                Text("Cancelar", color = Color.White)
            }
        }
    )
}

@Composable
fun SongItem(song: Song, onDelete: (String) -> Unit, onEdit: (Song) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp), // Margen en los laterales
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2E2E2E))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = song.title ?: "Sin título",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = song.anyo?.toString() ?: "Año no disponible",
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            IconButton(
                onClick = { onEdit(song) },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = "Editar",
                    tint = Color.Yellow,
                    modifier = Modifier.size(18.dp)
                )
            }

            IconButton(
                onClick = { onDelete(song.id ?: "") },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "Eliminar",
                    tint = Color.Red,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun LogoutDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1ED760),
        title = { Text("Cerrar Sesión", color = Color.Black) },
        text = { Text("¿Estás seguro de que deseas cerrar sesión?", color = Color.Black) },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E1E1E))
            ) {
                Text("Aceptar", color = Color.White)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E1E1E))
            ) {
                Text("Cancelar", color = Color.White)
            }
        }
    )
}