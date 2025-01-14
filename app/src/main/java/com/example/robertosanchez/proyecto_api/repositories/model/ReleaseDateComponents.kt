package com.example.robertosanchez.proyecto_api.repositories.model

import kotlinx.serialization.Serializable

@Serializable
data class ReleaseDateComponents(
    val day: Int,
    val month: Int,
    val year: Int
)