package com.cursokotlin.uptask.task.presentations.viewmodels

import androidx.lifecycle.ViewModel
import com.cursokotlin.uptask.task.presentations.screens.FiltroEstado
import com.cursokotlin.uptask.task.presentations.screens.FiltroPrioridad
import com.cursokotlin.uptask.task.presentations.screens.Orden
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

    //lista de tareas
    private var todasLasTareas: List<TareaUi> = emptyList()

    init {
        cargarDatosDePrueba()
    }

    fun cambiarBusqueda(texto: String) {
        _uiState.update { estadoActual -> estadoActual.copy(textoBusqueda = texto) }
        mostrarTareasFiltradas()
    }

    fun cambiarFiltroMateria() {
        val opciones: List<String?> = listOf<String?>(null) + todasLasTareas.map { it.materia }.distinct()
        val siguiente = siguienteOpcion(opciones, _uiState.value.filtroMateria)
        _uiState.update { estadoActual -> estadoActual.copy(filtroMateria = siguiente) }
        mostrarTareasFiltradas()
    }

    fun cambiarFiltroEstado() {
        val siguiente = siguienteOpcion(FiltroEstado.entries, _uiState.value.filtroEstado)
        _uiState.update { estadoActual -> estadoActual.copy(filtroEstado = siguiente) }
        mostrarTareasFiltradas()
    }

    fun cambiarFiltroPrioridad() {
        val siguiente = siguienteOpcion(FiltroPrioridad.entries, _uiState.value.filtroPrioridad)
        _uiState.update { estadoActual -> estadoActual.copy(filtroPrioridad = siguiente) }
        mostrarTareasFiltradas()
    }

    fun cambiarOrden() {
        val siguiente = siguienteOpcion(Orden.entries, _uiState.value.orden)
        _uiState.update { estadoActual -> estadoActual.copy(orden = siguiente) }
        mostrarTareasFiltradas()
    }

    // Marca una tarea como completada o pendiente (checkbox)
    fun cambiarEstadoTarea(id: Int, completada: Boolean) {
        todasLasTareas = todasLasTareas.map { tarea ->
            if (tarea.id == id) tarea.copy(completada = completada) else tarea
        }
        mostrarTareasFiltradas()
    }

    fun eliminarTarea(id: Int) {
        todasLasTareas = todasLasTareas.filter { tarea -> tarea.id != id }
        mostrarTareasFiltradas()
    }

    // Aplica búsqueda, filtros y orden a la lista completa y actualiza el estado
    private fun mostrarTareasFiltradas() {
        val estado = _uiState.value
        val tareasFiltradas = todasLasTareas.filter { tarea -> cumpleFiltros(tarea, estado) }
        val tareasOrdenadas = when (estado.orden) {
            Orden.FECHA_PROXIMA -> tareasFiltradas.sortedBy { it.diasParaVencer }
            Orden.FECHA_LEJANA -> tareasFiltradas.sortedByDescending { it.diasParaVencer }
            // ordinal es la posición en el enum: BAJA=0, MEDIA=1, ALTA=2
            Orden.PRIORIDAD -> tareasFiltradas.sortedByDescending { it.prioridad.ordinal }
        }

        _uiState.update { estadoActual ->
            estadoActual.copy(
                tareasPendientes = tareasOrdenadas.filter { !it.completada },
                tareasEntregadas = tareasOrdenadas.filter { it.completada }
            )
        }
    }

    private fun cumpleFiltros(tarea: TareaUi, estado: TareasUiState): Boolean {
        val cumpleBusqueda = tarea.titulo.contains(estado.textoBusqueda, ignoreCase = true)
        val cumpleMateria = estado.filtroMateria == null || tarea.materia == estado.filtroMateria
        val cumplePrioridad = when (estado.filtroPrioridad) {
            FiltroPrioridad.TODAS -> true
            FiltroPrioridad.ALTA -> tarea.prioridad == Prioridad.ALTA
            FiltroPrioridad.MEDIA -> tarea.prioridad == Prioridad.MEDIA
            FiltroPrioridad.BAJA -> tarea.prioridad == Prioridad.BAJA
        }

        val cumpleEstado = when (estado.filtroEstado) {
            FiltroEstado.TODAS -> true
            FiltroEstado.PENDIENTES -> !tarea.completada
            FiltroEstado.COMPLETADAS -> tarea.completada
        }

        return cumpleBusqueda && cumpleMateria && cumplePrioridad && cumpleEstado
    }

    private fun <T> siguienteOpcion(opciones: List<T>, actual: T): T {
        val posicion = opciones.indexOf(actual)
        return opciones[(posicion + 1) % opciones.size]
    }

    private fun cargarDatosDePrueba() {
        todasLasTareas = listOf(
            TareaUi(id = 1, titulo = "Row en Jetpack Compose", materia = "Apps Móviles", colorMateria = VerdeMateria, prioridad = Prioridad.ALTA, vence = "Vence Hoy", urgente = true, diasParaVencer = 0, completada = false),
            TareaUi(id = 2, titulo = "Diagrama de Clases UML", materia = "Análisis y Diseño", colorMateria = MoradoMateria, prioridad = Prioridad.MEDIA, vence = "Mañana", urgente = false, diasParaVencer = 1, completada = false),
            TareaUi(id = 3, titulo = "Contratos de API", materia = "Apps Web", colorMateria = AzulMateria, prioridad = Prioridad.MEDIA, vence = "En 2 días", urgente = false, diasParaVencer = 2, completada = false),
            TareaUi(id = 4, titulo = "Revisar rúbrica del proyecto", materia = "Apps Móviles", colorMateria = VerdeMateria, prioridad = Prioridad.ALTA, vence = "Atrasada", urgente = true, diasParaVencer = -1, completada = false),
            TareaUi(id = 5, titulo = "Wireframes de la app", materia = "Análisis y Diseño", colorMateria = MoradoMateria, prioridad = Prioridad.BAJA, vence = "En 5 días", urgente = false, diasParaVencer = 5, completada = false),
            TareaUi(id = 6, titulo = "Mockup UI en Figma", materia = "Apps Web", colorMateria = AzulMateria, prioridad = Prioridad.BAJA, vence = "", urgente = false, diasParaVencer = -3, completada = true),
            TareaUi(id = 7, titulo = "Configuración de StateFlow", materia = "Apps Móviles", colorMateria = VerdeMateria, prioridad = Prioridad.MEDIA, vence = "", urgente = false, diasParaVencer = -2, completada = true)
        )
        mostrarTareasFiltradas()
    }
}