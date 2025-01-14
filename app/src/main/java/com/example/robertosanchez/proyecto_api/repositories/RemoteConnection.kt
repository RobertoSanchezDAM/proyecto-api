package com.example.robertosanchez.proyecto_api.repositories

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RemoteConnection {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.genius.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: RemoteService = retrofit.create()
}