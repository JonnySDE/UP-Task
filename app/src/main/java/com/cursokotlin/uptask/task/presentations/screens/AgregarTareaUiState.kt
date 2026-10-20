package com.cursokotlin.uptask.task.presentations.screens

import androidx.compose.ui.graphics.Color

data class MateriaUi(
    val id: Int,
    val nombre: String,
    val color: Color
)

data class NuevaTarea(
    val titulo: String,
    val descripcion: String,
    val materia: MateriaUi,
    val prioridad: Prioridad,
    val fechaMillis: Long,
    val id: Int? = null
)

data class AgregarTareaUiState(
    val id: Int? = null,
    val titulo: String = "",
    val descripcion: String = "",
    val materias: List<MateriaUi> = emptyList(),
    val materiaSeleccionada: MateriaUi? = null,
    val menuMateriaAbierto: Boolean = false,
    val fechaMillis: Long = 0L,
    val fechaTexto: String = "",
    val calendarioVisible: Boolean = false,
    val prioridad: Prioridad = Prioridad.MEDIA,
    val errorTitulo: Boolean = false
) {

    val esEdicion: Boolean
        get() = id != null
}