package com.cursokotlin.uptask.task.presentations.viewmodels

import androidx.lifecycle.ViewModel
import com.cursokotlin.uptask.task.presentations.screens.EntregaUi
import com.cursokotlin.uptask.task.presentations.screens.InicioUiState
import com.cursokotlin.uptask.task.presentations.screens.Prioridad
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.roundToInt

class InicioViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(InicioUiState())
    val uiState: StateFlow<InicioUiState> = _uiState.asStateFlow()

    init {
        cargarDatosPrueba()
    }

    private fun cargarDatosPrueba() {
        val pendientes = 5
        val atrasadas = 2
        val completadas = 12

        _uiState.value = InicioUiState(
            nombreUsuario = "Jonathan",
            fechaHoy = "Viernes 16 de Octubre",
            pendientes = pendientes,
            atrasadas = atrasadas,
            completadas = completadas,
            cumplimiento = calcularCumplimiento(pendientes,completadas),
            proximasEntregas = listOf(
                    EntregaUi(
                        "Investigación Composables",
                        "Aplicaciones Móviles",
                        "Vence hoy",
                        Prioridad.ALTA
                    ),
                    EntregaUi(
                        "Contratos de API",
                        "Aplicaciones Web",
                        "Mañana",
                        Prioridad.MEDIA
                    ),
                    EntregaUi(
                        "Diapositivas de analisis",
                        "Análisis y Diseño de Software",
                        "En 2 dias",
                        Prioridad.BAJA
                    ),

            )
        )
    }

    private fun calcularCumplimiento(pendientes: Int, completadas: Int): Int {
        val total = pendientes + completadas
        if (total == 0) return 0
        return (completadas * 100f / total).roundToInt()
    }

}
