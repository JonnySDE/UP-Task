package com.cursokotlin.uptask.task.presentations.screens

import androidx.compose.ui.graphics.Color

data class TareaUi(
    val id: Int,
    val titulo: String,
    val materia: String,
    val colorMateria: Color,
    val prioridad: Prioridad,
    val vence: String,
    val urgente: Boolean,
    val completada: Boolean
)

// Todo lo que la página de Tareas necesita para dibujarse
data class TareasUiState(
    val textoBusqueda: String = "",
    val filtroMateria: String = "",
    val filtroEstado: String = "",
    val filtroPrioridad: String = "",
    val orden: String = "",
    val tareasPendientes: List<TareaUi> = emptyList(),
    val tareasEntregadas: List<TareaUi> = emptyList()
)