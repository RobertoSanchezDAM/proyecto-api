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
    private val userId = auth.getCurrentUser()?.uid

    fun getTransactions(): Flow<List<Song>> {
        return firestore.collection("songs")
            .whereEqualTo("userId", userId)
            .snapshots()
            .map { qs ->
                qs.documents.mapNotNull { ds ->
                    songDBToSong(ds.toObject(SongDB::class.java), ds.id)
                }
            }
    }

    private fun songDBToSong(songDB: SongDB?, id: String) =
        Song(
            id = id,
            userId = songDB?.userId,
            title = songDB?.title,
            anyo = songDB?.anyo,
        )

    suspend fun addSong(song: Song) {
        val songWithUserId = song.copy(userId = userId)
        firestore.collection("songs").add(songWithUserId).await()
    }

    suspend fun updateSong(song: Song) {
        if (song.id.isNullOrEmpty()) return

        val songRef = firestore.collection("songs").document(song.id)
        songRef.set(song.copy(userId = userId)).await()
    }

    suspend fun deleteSongById(songId: String) {
        firestore.collection("songs").document(songId).delete().await()
    }
}

