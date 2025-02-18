package com.example.robertosanchez.proyectoapi.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AlbumDB::class], version = 1)
abstract class AlbumDataBase: RoomDatabase() {
    abstract fun albumDao(): AlbumDao
}