package com.cursokotlin.uptask.task.presentations.screens

import androidx.compose.ui.graphics.Color
import com.cursokotlin.uptask.task.data.model.Prioridad
import com.cursokotlin.uptask.task.presentations.utils.TextoFecha

// Una entrega que se muestra en la lista "Próximas Entregas"
data class EntregaUi(
    val titulo: String,
    val materia: String,
    val vence: TextoFecha,
    val prioridad: Prioridad
)

// Una materia con su porcentaje de avance
data class MateriaRendimientoUi(
    val nombre: String,
    val avance: Int,
    val color: Color
)

// Todo lo que la pantalla de Inicio necesita para dibujarse
data class InicioUiState(
    val fechaHoy: String = "",
    val pendientes: Int = 0,
    val atrasadas: Int = 0,
    val completadas: Int = 0,
    val cumplimiento: Int = 0,
    val proximasEntregas: List<EntregaUi> = emptyList(),
    val rendimientoMaterias: List<MateriaRendimientoUi> = emptyList()
)