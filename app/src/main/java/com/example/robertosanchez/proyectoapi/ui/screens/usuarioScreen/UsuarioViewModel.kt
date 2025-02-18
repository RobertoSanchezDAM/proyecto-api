package com.example.robertosanchez.proyectoapi.ui.screens.usuarioScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.robertosanchez.proyectoapi.db.Album
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
            firestore.getSongsTransactions().collect { songs ->
                _uiState.update { uiState ->
                    uiState.copy(songs = songs, isLoading = false)
                }
            }
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            firestore.getAlbumsTransactions().collect { albums ->
                _uiState.update { uiState ->
                    uiState.copy(albums = albums, isLoading = false)
                }
            }
        }
    }

    // Canciones
    fun addSong(song: Song) {
        viewModelScope.launch {
            firestore.addSong(song)
            firestore.getSongsTransactions().collect { songs ->
                _uiState.update { uiState -> uiState.copy(songs = songs) }
            }
        }
    }

    fun deleteSongById(songId: String) {
        if (songId.isEmpty()) return
        viewModelScope.launch {
            firestore.deleteSongById(songId)
            firestore.getSongsTransactions().collect { songs ->
                _uiState.update { uiState -> uiState.copy(songs = songs) }
            }
        }
    }

    fun updateSong(song: Song) {
        viewModelScope.launch {
            firestore.updateSong(song)
            firestore.getSongsTransactions().collect { songs ->
                _uiState.update { uiState -> uiState.copy(songs = songs) }
            }
        }
    }

    // Albumes
    fun addAlbum(album: Album) {
        viewModelScope.launch {
            firestore.addAlbum(album)
            firestore.getAlbumsTransactions().collect { album ->
                _uiState.update { uiState -> uiState.copy(albums = album) }
            }
        }
    }

    fun deleteAlbumById(albumId: String) {
        if (albumId.isEmpty()) return
        viewModelScope.launch {
            firestore.deleteAlbumById(albumId)
            firestore.getAlbumsTransactions().collect { album ->
                _uiState.update { uiState -> uiState.copy(albums = album) }
            }
        }
    }

    fun updateAlbum(album: Album) {
        viewModelScope.launch {
            firestore.updateAlbum(album)
            firestore.getAlbumsTransactions().collect { album ->
                _uiState.update { uiState -> uiState.copy(albums = album) }
            }
        }
    }
}

data class UiState(
    val isLoading: Boolean = false,
    val songs: List<Song> = emptyList(),
    val albums: List<Album> = emptyList(),
    val showAddNoteDialog: Boolean = false,
    val showLogoutDialog: Boolean = false
)
