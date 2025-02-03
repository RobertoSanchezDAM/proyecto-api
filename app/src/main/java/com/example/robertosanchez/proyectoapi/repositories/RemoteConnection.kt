package com.example.robertosanchez.proyectoapi.repositories

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


object RemoteConnection {

    // Desactiva la verificación de hostname, manteniendo HTTPS.
    val unsafeOkHttpClient = OkHttpClient.Builder()
        .hostnameVerifier { _, _ -> true }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.genius.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(unsafeOkHttpClient)
        .build()

    val service: RemoteService = retrofit.create()
}