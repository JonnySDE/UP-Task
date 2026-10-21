package com.cursokotlin.uptask.task.di

import android.content.Context
import com.cursokotlin.uptask.task.data.datasource.UpTaskLocalDataSource
import com.cursokotlin.uptask.task.data.repository.UpTaskRepository
import com.cursokotlin.uptask.task.data.repository.UpTaskRepositoryImpl
import com.cursokotlin.uptask.task.presentations.viewmodels.UpTaskViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

object UpTaskModule {

    // Una sola instancia del repositorio para toda la app
    private var repositorio: UpTaskRepository? = null

    private fun proveerRepositorio(context: Context): UpTaskRepository {
        val existente = repositorio
        if (existente != null) return existente

        val dataSource = UpTaskLocalDataSource(context.applicationContext)
        val nuevo = UpTaskRepositoryImpl(dataSource)

        // Carga las materias y tareas semilla la primera vez que se abre la app
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            dataSource.cargarSemillaSiEsNecesario()
        }

        repositorio = nuevo
        return nuevo
    }

    fun proveerFactory(context: Context): UpTaskViewModelFactory {
        return UpTaskViewModelFactory(proveerRepositorio(context))
    }
}