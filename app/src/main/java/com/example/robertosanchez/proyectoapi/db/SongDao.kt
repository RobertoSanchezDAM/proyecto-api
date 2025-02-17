package com.example.robertosanchez.proyectoapi.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SongDao {
    @Insert
    suspend fun insert(song: Song)

}