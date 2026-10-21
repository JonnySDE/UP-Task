package com.cursokotlin.uptask.task.data.model

data class Materia(
    val id: Int,
    val nombre: String,
    val color: String? = null,
    val imagenUri: String? = null,
    val archivada: Boolean = false,
    val esInicial: Boolean = false
)