package com.example.robertosanchez.proyectoapi.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.robertosanchez.proyectoapi.model.MediaItem
import com.example.robertosanchez.proyectoapi.repositories.model.PrimaryArtist
import com.example.robertosanchez.proyectoapi.repositories.model.ReleaseDateComponents

@Entity
data class AlbumDB(
    @PrimaryKey(autoGenerate = true) val id: String? = null,
    val userId: String = "",
    val title: String = "",
    val anyo: Int = 0,
    val numCanciones: Int = 0
)

