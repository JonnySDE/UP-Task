package com.cursokotlin.uptask.task.presentations.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cursokotlin.uptask.task.data.model.Materia
import com.cursokotlin.uptask.task.data.model.Prioridad
import com.cursokotlin.uptask.task.data.model.Tarea
import com.cursokotlin.uptask.task.data.repository.UpTaskRepository
import com.cursokotlin.uptask.task.presentations.screens.FiltroEstado
import com.cursokotlin.uptask.task.presentations.screens.FiltroPrioridad
import com.cursokotlin.uptask.task.presentations.screens.NuevaTarea
import com.cursokotlin.uptask.task.presentations.screens.Orden
import com.cursokotlin.uptask.task.presentations.screens.TareaUi
import com.cursokotlin.uptask.task.presentations.screens.TareasUiState
import com.cursokotlin.uptask.task.presentations.utils.aColor
import com.cursokotlin.uptask.task.presentations.utils.MILISEGUNDOS_POR_DIA
import com.cursokotlin.uptask.task.presentations.utils.hoyEnMillisUtc
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.cursokotlin.uptask.task.presentations.utils.textoVencimiento

class TareasViewModel(
    private val repository: UpTaskRepository
) : ViewModel() {

    // _uiState es privado: solo el ViewModel lo puede cambiar
    private val _uiState = MutableStateFlow(TareasUiState())

    // uiState es público y de solo lectura: la página solo lo observa
    val uiState: StateFlow<TareasUiState> = _uiState.asStateFlow()

    // La lista COMPLETA de tareas, sin filtrar. Solo el ViewModel la conoce.
    private var todasLasTareas: List<TareaUi> = emptyList()

    init {
        observarDatos()
    }

    // Escucha al Repository. Cada vez que cambian las tareas o las materias, se recalcula la lista.
    private fun observarDatos() {
        viewModelScope.launch {
            combine(
                repository.obtenerTareas(),
                repository.obtenerMaterias()
            ) { tareas, materias ->
                convertirATareasUi(tareas, materias)
            }.collect { tareasUi ->
                todasLasTareas = tareasUi
                mostrarTareasFiltradas()
            }
        }
    }

    // Convierte las tareas del Repository en tareas listas para mostrar
    private fun convertirATareasUi(tareas: List<Tarea>, materias: List<Materia>): List<TareaUi> {
        val hoy = hoyEnMillisUtc()

        return tareas.map { tarea ->
            val materia = materias.firstOrNull { it.id == tarea.materiaId }
            val dias = ((tarea.fechaEntrega - hoy) / MILISEGUNDOS_POR_DIA).toInt()

            TareaUi(
                id = tarea.id,
                titulo = tarea.titulo,
                materiaId = tarea.materiaId,
                materia = materia?.nombre ?: "",
                colorMateria = materia?.color.aColor(),
                prioridad = tarea.prioridad,
                vence = textoVencimiento(dias),
                urgente = !tarea.completada && dias <= 0,
                diasParaVencer = dias,
                completada = tarea.completada,
                descripcion = tarea.descripcion ?: ""
            )
        }
    }

    // ---------- EVENTOS: la página llama a estas funciones ----------

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

    // Marca una tarea como completada o pendiente (el checkbox).
    // No se toca la lista aquí: el Repository guarda y el Flow avisa del cambio.
    fun cambiarEstadoTarea(id: Int, completada: Boolean) {
        viewModelScope.launch {
            repository.cambiarEstadoTarea(id, completada)
        }
    }

    fun eliminarTarea(id: Int) {
        viewModelScope.launch {
            repository.eliminarTarea(id)
        }
    }

    // Recibe lo que el formulario guardó. Si trae id es una edición, si no, es una tarea nueva.
    fun guardarTarea(datos: NuevaTarea) {
        viewModelScope.launch {
            repository.guardarTarea(
                Tarea(
                    id = datos.id ?: 0,
                    titulo = datos.titulo,
                    descripcion = datos.descripcion.ifBlank { null },
                    materiaId = datos.materia.id,
                    fechaEntrega = datos.fechaMillis,
                    prioridad = datos.prioridad,
                    // Las fechas de creación y actualización las asigna el Repository
                    fechaCreada = 0L,
                    fechaActu = 0L
                )
            )
        }
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

    // Devuelve la opción que sigue en la lista. Después de la última vuelve a la primera.
    private fun <T> siguienteOpcion(opciones: List<T>, actual: T): T {
        val posicion = opciones.indexOf(actual)
        return opciones[(posicion + 1) % opciones.size]
    }
}