package com.example.robertosanchez.proyecto_api.ui.screens.listaScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.robertosanchez.proyecto_api.model.MediaItem
import com.example.robertosanchez.proyecto_api.repositories.RemoteConnection
import com.example.robertosanchez.proyecto_api.repositories.model.FeaturedArtist
import com.example.robertosanchez.proyecto_api.repositories.model.PrimaryArtist
import kotlinx.coroutines.launch

class ListaViewModel(private var cantante: String) : ViewModel() {

    // Almacena la lista completa de canciones
    val _listaCompleta: MutableList<MediaItem> = mutableListOf()

    val _listaPag: MutableLiveData<List<MediaItem>> = MutableLiveData()
    val listaPag: LiveData<List<MediaItem>> = _listaPag

    val _progressBar: MutableLiveData<Boolean> = MutableLiveData(false)
    val progressBar: LiveData<Boolean> = _progressBar

    val itemsPorPagina = 5
    var paginaActual = 0

    init {
        cargarDatos(cantante)
    }

    fun cargarDatos(cantante: String) {
        _progressBar.value = true
        viewModelScope.launch {
            val songs = RemoteConnection.service.searchSong(
                cantante,
                "Bearer m4gMiFYz7x-mVJG-IbaaY4qyVJwPEqtG34hZ9-Ux_KYjBJMNjBwsclgAfDoknpoN"
            )

            // Agregar las canciones a la lista completa
            _listaCompleta.addAll(
                songs.response.hits.map {
                    MediaItem(
                        id = it.result.id,
                        name = it.result.title,
                        image = it.result.header_image_url,
                        artists = it.result.primary_artists,
                        released_date = it.result.release_date_components,
                        type = it.type
                    )
                }
            )

            _progressBar.value = false
            cargarMas()
        }
    }


    fun cargarMas() {
        // Calcula el índice inicial y final para mostrar la lista actual
        val indiceInicial = paginaActual * itemsPorPagina
        val indiceFinal = (indiceInicial + itemsPorPagina).coerceAtMost(_listaCompleta.size)

        // Si el índice inicial es menor al índice final, significa que
        // hay elementos adicionales para mostrar
        if (indiceInicial < indiceFinal) {
            // Función (sublist) toma los elementos de la lista completa que están
            // dentro de un rango (indiceInicial, indiceFinal) y se agrega a la lista de paginada
            _listaPag.value = _listaPag.value.orEmpty() + _listaCompleta.subList(indiceInicial, indiceFinal)

            // Se incrementa la página actual para mostrar los siguientes elementos
            paginaActual++
        }
    }

    fun cargarMenos() {
        // Si la pagina actual es mayor que 0, significa que no hay elementos adicionales que quitar
        if (paginaActual > 0) {

            // Se resta 1 a la pagina actual para indicar que queremos volver a
            // mostrar los primeros elementos
            paginaActual--

            // Se calcula el indice final para mostrar la lista actual
            val indiceFinal = paginaActual * itemsPorPagina

            // Funcion que devuelve una lista de la lista actual que contiene
            // los primeros items (indiceFinal)
            _listaPag.value = _listaPag.value.orEmpty().take(indiceFinal)
        }
    }

    fun completa(): Boolean {
        // Si la página actual por el número de items de la página es mayor que el tamaño
        // completo de la lista completa, entonces la lista está completa
        return paginaActual * itemsPorPagina >= _listaCompleta.size
    }
}
