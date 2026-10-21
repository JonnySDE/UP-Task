package com.cursokotlin.uptask.task.data.repository

import com.cursokotlin.uptask.task.data.model.Materia
import com.cursokotlin.uptask.task.data.model.Tarea
import kotlinx.coroutines.flow.Flow

interface UpTaskRepository {
    fun obtenerMaterias(): Flow<List<Materia>>
    fun obtenerTareas(): Flow<List<Tarea>>

    suspend fun guardarMateria(materia: Materia)
    suspend fun alternarArchivada(id: Int)

    suspend fun guardarTarea(tarea: Tarea)
    suspend fun cambiarEstadoTarea(id: Int, completada: Boolean)
    suspend fun eliminarTarea(id: Int)
}