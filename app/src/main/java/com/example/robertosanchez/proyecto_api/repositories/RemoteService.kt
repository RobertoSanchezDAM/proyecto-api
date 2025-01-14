package com.example.robertosanchez.proyecto_api.repositories


import com.example.robertosanchez.proyecto_api.repositories.model.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface RemoteService {
    @GET("search")
    suspend fun searchSong(
        @Query("q") query: String, // Parámetro de búsqueda
        @Header("Authorization") authHeader: String // Encabezado de autorización
    ): Response
}