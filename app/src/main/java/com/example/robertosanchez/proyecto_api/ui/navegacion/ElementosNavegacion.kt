package com.example.robertosanchez.proyecto_api.ui.navegacion

import kotlinx.serialization.Serializable

@Serializable
object Inicio

@Serializable
object Buscar

@Serializable
data class Lista(val cantante: String)

@Serializable
data class Detail(val id: Int, val cantante: String)