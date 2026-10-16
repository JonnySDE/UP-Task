package com.cursokotlin.uptask.task.presentations.screens

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.cursokotlin.uptask.R

// Opciones del filtro de estado. Cada una trae su texto.
enum class FiltroEstado(@StringRes val texto: Int) {
    TODAS(R.string.filtro_todas),
    PENDIENTES(R.string.estado_pendientes),
    COMPLETADAS(R.string.estado_completadas)
}

// Opciones del filtro de prioridad
enum class FiltroPrioridad(@StringRes val texto: Int) {
    TODAS(R.string.filtro_todas),
    ALTA(R.string.prioridad_alta),
    MEDIA(R.string.prioridad_media),
    BAJA(R.string.prioridad_baja)
}

// Formas de ordenar la lista
enum class Orden(@StringRes val texto: Int) {
    FECHA_PROXIMA(R.string.orden_fecha_proxima),
    FECHA_LEJANA(R.string.orden_fecha_lejana),
    PRIORIDAD(R.string.orden_prioridad)
}


data class TareaUi(
    val id: Int,
    val titulo: String,
    val materia: String,
    val colorMateria: Color,
    val prioridad: Prioridad,
    val vence: String,
    val urgente: Boolean,
    val diasParaVencer: Int,
    val completada: Boolean
)

// Todo lo que la página de Tareas necesita para dibujarse
data class TareasUiState(
    val textoBusqueda: String = "",
    val filtroMateria: String? = null,
    val filtroEstado: FiltroEstado = FiltroEstado.TODAS,
    val filtroPrioridad: FiltroPrioridad = FiltroPrioridad.TODAS,
    val orden: Orden = Orden.FECHA_PROXIMA,
    val tareasPendientes: List<TareaUi> = emptyList(),
    val tareasEntregadas: List<TareaUi> = emptyList()
)