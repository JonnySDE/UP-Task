package com.cursokotlin.uptask.task.presentations.screens

import androidx.compose.ui.graphics.Color

// Una materia que se puede elegir en el formulario
data class MateriaUi(
    val id: Int,
    val nombre: String,
    val color: Color
)

// Los datos que el formulario entrega cuando se guarda
data class NuevaTarea(
    val titulo: String,
    val descripcion: String,
    val materia: MateriaUi,
    val prioridad: Prioridad,
    val fechaMillis: Long
)

// Todo lo que el formulario necesita para dibujarse
data class AgregarTareaUiState(
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
)