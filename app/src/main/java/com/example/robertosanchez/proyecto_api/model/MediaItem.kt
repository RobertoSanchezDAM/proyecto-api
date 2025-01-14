package com.example.robertosanchez.proyecto_api.model

import com.example.robertosanchez.proyecto_api.repositories.model.FeaturedArtist
import com.example.robertosanchez.proyecto_api.repositories.model.PrimaryArtist
import com.example.robertosanchez.proyecto_api.repositories.model.ReleaseDateComponents
import kotlinx.serialization.Serializable

@Serializable
data class MediaItem(
    val id: Int,
    val name: String,
    val image: String,
    val artists: List<PrimaryArtist>,
    val released_date: ReleaseDateComponents,
    val type: String
)
