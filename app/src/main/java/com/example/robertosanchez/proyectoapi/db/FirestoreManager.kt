package com.example.robertosanchez.proyectoapi.db

import android.content.Context
import com.example.robertosanchez.proyectoapi.data.AuthManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class FirestoreManager(auth: AuthManager, context: Context) {
    private val firestore = FirebaseFirestore.getInstance()
    private val userId = auth.getCurrentUser()?.uid // Obtener el userId del usuario autenticado

    // Obtener las canciones filtradas por el userId del usuario
    fun getTransactions(): Flow<List<Song>> {
        return firestore
            .collection("songs")
            .whereEqualTo("userId", userId) // Filtrar las canciones por el userId
            .snapshots()
            .map { qs ->
                qs.documents.mapNotNull { ds ->
                    songDBToSong(
                        ds.toObject(SongDB::class.java),
                        ds.id
                    )
                }
            }
    }

    // Convertir de SongDB a Song
    private fun songDBToSong(songDB: SongDB?, id: String) =
        Song(
            id = id,
            userId = songDB?.userId,
            title = songDB?.title,
            anyo = songDB?.anyo,
        )

    // Agregar una canción y asociarla con el userId
    suspend fun addSong(song: Song) {
        val songWithUserId = song.copy(userId = userId) // Asociar el userId al agregar la canción
        firestore.collection("songs").add(songWithUserId).await() // Agregar la canción
    }

    // Actualizar una canción
    suspend fun updateSong(song: Song) {
        val songRef = song.id?.let {
            firestore.collection("songs").document(it)
        }
        songRef?.set(song.copy(userId = userId))?.await() // Asegurarse de que el userId esté asociado
    }

    // Eliminar una canción por su ID
    suspend fun deleteSongById(songId: String) {
        firestore.collection("songs").document(songId).delete().await()
    }
}
