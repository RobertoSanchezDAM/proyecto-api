package com.example.robertosanchez.proyecto_api

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.robertosanchez.proyecto_api.ui.navegacion.Navegacion
import com.example.robertosanchez.proyecto_api.ui.theme.ProyectoapiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            ProyectoapiTheme {
                Navegacion()
            }
        }
    }
}