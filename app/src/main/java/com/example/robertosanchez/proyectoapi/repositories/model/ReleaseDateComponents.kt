package com.example.robertosanchez.proyectoapi.repositories.model

import kotlinx.serialization.Serializable

@Serializable
data class ReleaseDateComponents(
    val day: Int,
    val month: Int,
    val year: Int
)