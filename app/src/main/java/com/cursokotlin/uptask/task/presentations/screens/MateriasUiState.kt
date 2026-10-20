package com.cursokotlin.uptask.task.presentations.screens

import androidx.compose.ui.graphics.Color

data class MateriaResumenUi(
    val id: Int,
    val nombre: String,
    val color: Color,
    val pendientes: Int,
    val completadas: Int,
    val cumplimiento: Int,
    val archivada: Boolean,
    val imagenUri: String? = null
)

data class MateriasUiState(
    val mostrandoArchivadas: Boolean = false,
    val totalActivas: Int = 0,
    val totalArchivadas: Int = 0,
    val materiasMostradas: List<MateriaResumenUi> = emptyList()
)