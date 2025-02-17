package com.example.robertosanchez.proyectoapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import com.example.robertosanchez.proyectoapi.data.AuthManager
import com.example.robertosanchez.proyectoapi.db.SongDataBase
import com.example.robertosanchez.proyectoapi.ui.navegacion.Navegacion
import com.example.robertosanchez.proyectoapi.ui.theme.ProyectoapiTheme
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    val auth = AuthManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        Firebase.analytics
        setContent {
            ProyectoapiTheme {
                Navegacion(auth)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        auth.signOut()
    }
}