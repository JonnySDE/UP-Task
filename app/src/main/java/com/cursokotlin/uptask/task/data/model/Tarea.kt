package com.cursokotlin.uptask.task.data.model

data class Tarea(
    val id: Int,
    val titulo: String,
    val descripcion: String? = null,
    val materiaId: Int,
    val fechaEntrega: Long,
    val prioridad: Prioridad = Prioridad.MEDIA,
    val completada: Boolean = false,
    val fechaCreada: Long,
    val fechaActu: Long
)