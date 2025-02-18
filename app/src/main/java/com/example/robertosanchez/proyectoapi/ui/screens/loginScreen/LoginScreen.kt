package com.example.robertosanchez.proyectoapi.ui.screens.loginScreen

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.robertosanchez.proyectoapi.R
import com.example.robertosanchez.proyectoapi.data.AuthManager
import com.example.robertosanchez.proyectoapi.data.AuthRes
import com.example.robertosanchez.proyectoapi.ui.theme.Purple40
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun LoginScreen(
    auth: AuthManager,
    navigateToSignUp: () -> Unit,
    navigateToUsuario: () -> Unit,
    navigateToContraseñaOlv: () -> Unit,
    onBack: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val googleSignLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        when (val account =
            auth.handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(result.data))) {
            is AuthRes.Success -> {
                val credential = GoogleAuthProvider.getCredential(account.data?.idToken, null)
                scope.launch {
                    val firebaseUser = auth.googleSignInCredential(credential)
                    when (firebaseUser) {
                        is AuthRes.Success -> {
                            Toast.makeText(
                                context,
                                "Inicio de sesión correcto",
                                Toast.LENGTH_SHORT
                            ).show()
                            navigateToUsuario()
                        }

                        is AuthRes.Error -> {
                            Toast.makeText(
                                context,
                                "Error al iniciar sesión",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
            is AuthRes.Error -> {
                Toast.makeText(context, "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
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

        // Contenido de la pantalla
        Column(
            modifier = Modifier.fillMaxSize()
                .background(Color.Black.copy(alpha = 0.8f)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Firebase",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    scope.launch {
                        signIn(email, password, context, auth, navigateToUsuario)
                    }
                },
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1ED760)
                )
            ) {
                Text(
                    text = "Iniciar Sesión".uppercase(),
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "¿Olvidaste tu contraseña?",
                modifier = Modifier.clickable { navigateToContraseñaOlv() },
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Default,
                    textDecoration = TextDecoration.Underline,
                    color = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(25.dp))

            Text(text = "-------- o --------", style = TextStyle(color = Color.Gray))

            Spacer(modifier = Modifier.height(25.dp))

            SocialMediaButton(
                onClick = {
                    scope.launch {
                        signAnonimous(auth, navigateToUsuario, context)
                    }
                },
                text = "Continuar como invitado",
                icon = R.drawable.ic_incognito,
                color = Color(0xFF363636)
            )

            Spacer(modifier = Modifier.height(15.dp))

            SocialMediaButton(
                onClick = {
                    auth.signInWithGoogle(googleSignLauncher)
                },
                text = "Continuar con Google",
                icon = R.drawable.ic_google,
                color = Color(0xFFF1F1F1)
            )
        }

        // Texto de registro al final
        Text(
            text = "¿No tienes cuenta? Regístrate",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
                .clickable { navigateToSignUp() },
            style = TextStyle(
                color = Color.Gray,
                fontSize = 14.sp,
                fontFamily = FontFamily.Default,
                textDecoration = TextDecoration.Underline
            )
        )
    }
}


suspend fun signAnonimous(auth: AuthManager, navigateToHome: () -> Unit, context: Context) {
    val res = withContext(Dispatchers.IO) {
        auth.signInAnonymously()
    }
    when (res) {
        is AuthRes.Success -> {
            Toast.makeText(context, "Inicio de sesión correcto", Toast.LENGTH_SHORT).show()
            navigateToHome()
        }
        is AuthRes.Error -> {
            Toast.makeText(context, res.errorMessage, Toast.LENGTH_SHORT).show()
        }
    }
}

suspend fun signIn(email: String, password: String, context: Context, auth: AuthManager, navigateToHome: () -> Unit) {
    if (email.isNotEmpty() && password.isNotEmpty()) {
        val result =
            withContext(Dispatchers.IO) {
                auth.signInWithEmailAndPassword(email, password)
            }
            when (result) {
                is AuthRes.Success -> {
                    Toast.makeText(context, "Inicio de sesión correcto", Toast.LENGTH_SHORT).show()
                    navigateToHome()
                }
                is AuthRes.Error -> {
                    Toast.makeText(context, result.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

    } else{
        Toast.makeText(context, "Email y password tienen que estar rellenos", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun SocialMediaButton(onClick: () -> Unit, text: String, icon: Int, color: Color, ) {
    var click by remember { mutableStateOf(false) }
    Surface(
        onClick = onClick,
        modifier = Modifier.padding(start = 40.dp, end = 40.dp).clickable { click = !click },
        shape = RoundedCornerShape(50),
        border = BorderStroke(width = 1.dp, color = if(icon == R.drawable.ic_incognito) color else Color.Gray),
        color = color
    ) {
        Row(
            modifier = Modifier.padding(start = 12.dp, end = 16.dp, top = 12.dp, bottom = 12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                modifier = Modifier.size(24.dp),
                contentDescription = text,
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "$text", color = if(icon == R.drawable.ic_incognito) Color.White else Color.Black)
            click = true
        }
    }
}
