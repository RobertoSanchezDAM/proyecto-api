package com.example.robertosanchez.proyectoapi.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SongDB::class], version = 1)
abstract class SongDataBase: RoomDatabase() {
    abstract fun songDao(): SongDao
}