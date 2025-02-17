package com.example.robertosanchez.proyectoapi.db

import com.example.robertosanchez.proyectoapi.repositories.model.PrimaryArtist

data class Song(
    val id: String = "",
    val userId: String?,
    val title: String?,
    val anyo: Int?,
)
