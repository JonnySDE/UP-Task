package com.cursokotlin.uptask.task.presentations.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cursokotlin.uptask.task.data.repository.UpTaskRepository

class UpTaskViewModelFactory(
    private val repository: UpTaskRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InicioViewModel::class.java)) {
            return InicioViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(TareasViewModel::class.java)) {
            return TareasViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(MateriasViewModel::class.java)) {
            return MateriasViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(AgregarTareaViewModel::class.java)) {
            return AgregarTareaViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(AgregarMateriaViewModel::class.java)) {
            return AgregarMateriaViewModel(repository) as T
        }
        throw IllegalArgumentException("Clase ViewModel desconocida")
    }
}