package com.example.robertosanchez.proyectoapi.ui.navegacion

import kotlinx.serialization.Serializable

@Serializable
object Inicio

@Serializable
object Login

@Serializable
object Usuario

@Serializable
object Buscar

@Serializable
object ContraseñaOlv

@Serializable
object Registro

@Serializable
data class Lista(val cantante: String)

@Serializable
data class Detail(val id: Int, val cantante: String)