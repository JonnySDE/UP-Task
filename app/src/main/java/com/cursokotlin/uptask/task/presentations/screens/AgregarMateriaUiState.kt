package com.cursokotlin.uptask.task.presentations.screens

import androidx.compose.ui.graphics.Color

// Los datos que el formulario entrega cuando se guarda.
// Si id es null, es una materia nueva. Si trae id, es una edición.
data class MateriaFormulario(
    val id: Int?,
    val nombre: String,
    val color: Color,
    val imagenUri: String?,
    val archivada: Boolean
)


data class AgregarMateriaUiState(
    val id: Int? = null,
    val nombre: String = "",
    val colores: List<Color> = emptyList(),
    val colorSeleccionado: Color = Color.Unspecified,
    val imagenUri: String? = null,
    val archivada: Boolean = false,
    val errorNombre: Boolean = false
) {
    // Es edición cuando la materia ya tiene id
    val esEdicion: Boolean
        get() = id != null
}