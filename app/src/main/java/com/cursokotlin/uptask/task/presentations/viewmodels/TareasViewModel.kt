package com.cursokotlin.uptask.task.presentations.viewmodels

import androidx.lifecycle.ViewModel
import com.cursokotlin.uptask.task.presentations.screens.Prioridad
import com.cursokotlin.uptask.task.presentations.screens.TareaUi
import com.cursokotlin.uptask.task.presentations.screens.TareasUiState
import com.cursokotlin.uptask.ui.theme.AzulMateria
import com.cursokotlin.uptask.ui.theme.MoradoMateria
import com.cursokotlin.uptask.ui.theme.VerdeMateria
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TareasViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TareasUiState())

    val uiState: StateFlow<TareasUiState> = _uiState.asStateFlow()

    init {
        cargarDatosDePrueba()
    }

    // Cada vez que el usuario escribe, se crea un estado nuevo con el texto actualizado
    fun cambiarBusqueda(texto: String) {
        _uiState.update { estadoActual -> estadoActual.copy(textoBusqueda = texto) }
    }

    // Datos temporales.
    private fun cargarDatosDePrueba() {
        _uiState.value = TareasUiState(
            filtroMateria = "Todas",
            filtroEstado = "Pendiente",
            filtroPrioridad = "Todas",
            orden = "Fecha de entrega (Próximas)",
            tareasPendientes = listOf(
                TareaUi(1, "Investigación Composables", "Aplicaciones Móviles", VerdeMateria, Prioridad.ALTA, "Vence Hoy", true, false),
                TareaUi(2, "Dipositivas de analisis", "Análisis y Diseño", MoradoMateria, Prioridad.MEDIA, "2 dias", false, false),
                TareaUi(3, "Contratos de API", "Aplicaciones Web", MoradoMateria, Prioridad.MEDIA, "Mañana", false, false)
            ),
            tareasEntregadas = listOf(
                TareaUi(4, "Mockup UI en Figma", "Apps Web", AzulMateria, Prioridad.BAJA, "", false, true)
            )
        )
    }
}