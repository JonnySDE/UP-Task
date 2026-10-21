package com.cursokotlin.uptask.task.presentations.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cursokotlin.uptask.task.data.repository.UpTaskRepository

class UpTaskViewModelFactory(
    private val repository: UpTaskRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TareasViewModel::class.java)) {
            return TareasViewModel(repository) as T
        }
        throw IllegalArgumentException("Clase ViewModel desconocida")
    }
}