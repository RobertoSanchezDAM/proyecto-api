package com.example.robertosanchez.proyectoapi.ui.screens.usuarioScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.robertosanchez.proyectoapi.R
import com.example.robertosanchez.proyectoapi.data.AuthManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuarioScreen(auth: AuthManager, navigateToLogin: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    val user = auth.getCurrentUser()

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if(user?.photoUrl != null){
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(user?.photoUrl)
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

                            Spacer(modifier = Modifier.width(2.dp))

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
                    IconButton(
                        onClick = {
                        showDialog = true }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Outlined.ExitToApp,
                            contentDescription = "Cerrar sesión",
                            tint = Color.Black
                        )
                    }
                }
            )
        }
    ){
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(it)
                .background(Color(0xFF1E1E1E))
        ){
            if (showDialog){
                LogoutDialog(
                    onDismiss = { showDialog = false },
                    onConfirm = {
                        auth.signOut()
                        navigateToLogin()
                        showDialog = false
                    }
                )
            }

            Text(
                text = "Por implementar",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun LogoutDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1ED760),
        title = {
            Text("Cerrar Sesión",
                color = Color.Black,
            )
        },

        text = {
            Text(
                "¿Estás seguro de que deseas cerrar sesión?",
                color = Color.Black,
            )
        },

        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1E1E1E)
                )
            ) {
                Text(
                    "Aceptar",
                    color = Color.White,
                )
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1E1E1E)
                )
            ) {
                Text(
                    "Cancelar",
                    color = Color.White,
                )
            }
        }
    )
}
