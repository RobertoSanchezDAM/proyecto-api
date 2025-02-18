package com.example.robertosanchez.proyectoapi.db

import com.example.robertosanchez.proyectoapi.repositories.model.PrimaryArtist

data class Album(
    val id: String = "",
    val userId: String?,
    val title: String?,
    val anyo: Int?,
    val numCanciones: Int?,
)
