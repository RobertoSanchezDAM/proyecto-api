package com.example.robertosanchez.proyectoapi.repositories.model

data class Hit(
    val highlights: List<Any>,
    val index: String,
    val result: Result,
    val type: String
)