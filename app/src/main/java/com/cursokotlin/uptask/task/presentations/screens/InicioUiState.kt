package com.cursokotlin.uptask.task.presentations.screens

// Una entrega que se muestra en la lista "Próximas Entregas"
data class EntregaUi(
    val titulo: String,
    val materia: String,
    val vence: String,
    val prioridad: Prioridad
)

// Todo lo que la pantalla de Inicio necesita para dibujarse
data class InicioUiState(
    val nombreUsuario: String = "",
    val fechaHoy: String = "",
    val pendientes: Int = 0,
    val atrasadas: Int = 0,
    val completadas: Int = 0,
    val cumplimiento: Int = 0,
    val proximasEntregas: List<EntregaUi> = emptyList()
)