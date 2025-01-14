package com.example.robertosanchez.proyecto_api.repositories.model

data class Hit(
    val highlights: List<Any>,
    val index: String,
    val result: Result,
    val type: String
)