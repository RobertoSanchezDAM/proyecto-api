package com.example.robertosanchez.proyecto_api.ui.screens.listaScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ListaViewModelFactory(private val cantante: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListaViewModel::class.java)) {
            return ListaViewModel(cantante) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
