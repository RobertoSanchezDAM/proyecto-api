package com.example.robertosanchez.proyectoapi.ui.screens.usuarioScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.robertosanchez.proyectoapi.db.FirestoreManager
import com.example.robertosanchez.proyectoapi.db.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UsuarioViewModel(private val firestore: FirestoreManager) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            firestore.getTransactions().collect { songs ->
                _uiState.update { uiState ->
                    uiState.copy(songs = songs, isLoading = false)
                }
            }
        }
    }

    fun addSong(song: Song) {
        viewModelScope.launch {
            firestore.addSong(song)
            firestore.getTransactions().collect { songs ->
                _uiState.update { uiState -> uiState.copy(songs = songs) }
            }
        }
    }

    fun deleteSongById(songId: String) {
        if (songId.isEmpty()) return
        viewModelScope.launch {
            firestore.deleteSongById(songId)
            firestore.getTransactions().collect { songs ->
                _uiState.update { uiState -> uiState.copy(songs = songs) }
            }
        }
    }

    fun updateSong(song: Song) {
        viewModelScope.launch {
            firestore.updateSong(song)  // Llamada a Firestore para actualizar la canción
            firestore.getTransactions().collect { songs ->
                _uiState.update { uiState -> uiState.copy(songs = songs) }
            }
        }
    }
}

data class UiState(
    val isLoading: Boolean = false,
    val songs: List<Song> = emptyList(),
    val showAddNoteDialog: Boolean = false,
    val showLogoutDialog: Boolean = false
)
