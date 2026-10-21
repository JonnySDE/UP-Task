package com.cursokotlin.uptask.task.presentations.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cursokotlin.uptask.task.data.model.Materia
import com.cursokotlin.uptask.task.data.model.Tarea
import com.cursokotlin.uptask.task.data.repository.UpTaskRepository
import com.cursokotlin.uptask.task.presentations.screens.EntregaUi
import com.cursokotlin.uptask.task.presentations.screens.InicioUiState
import com.cursokotlin.uptask.task.presentations.screens.MateriaRendimientoUi
import com.cursokotlin.uptask.task.presentations.utils.aColor
import com.cursokotlin.uptask.task.presentations.utils.calcularPorcentaje
import com.cursokotlin.uptask.task.presentations.utils.textoVencimiento
import com.cursokotlin.uptask.task.presentations.utils.MILISEGUNDOS_POR_DIA
import com.cursokotlin.uptask.task.presentations.utils.fechaDeHoyTexto
import com.cursokotlin.uptask.task.presentations.utils.hoyEnMillisUtc
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

// Cuántas entregas se muestran en "Próximas Entregas"
private const val MAXIMO_ENTREGAS = 5

class InicioViewModel(
    private val repository: UpTaskRepository
) : ViewModel() {

    // _uiState es privado: solo el ViewModel lo puede cambiar
    private val _uiState = MutableStateFlow(InicioUiState())

    // uiState es público y de solo lectura: la página solo lo observa
    val uiState: StateFlow<InicioUiState> = _uiState.asStateFlow()

    init {
        observarDatos()
    }

    // Escucha al Repository. Cada vez que cambian las tareas o las materias, se arma un estado nuevo.
    private fun observarDatos() {
        viewModelScope.launch {
            combine(
                repository.obtenerTareas(),
                repository.obtenerMaterias()
            ) { tareas, materias ->
                crearEstado(tareas, materias)
            }.collect { nuevoEstado ->
                _uiState.value = nuevoEstado
            }
        }
    }

    // Aquí está toda la lógica de negocio de la pantalla de Inicio
    private fun crearEstado(tareas: List<Tarea>, materias: List<Materia>): InicioUiState {
        val hoy = hoyEnMillisUtc()
        val pendientes = tareas.filter { !it.completada }
        val completadas = tareas.count { it.completada }

        // Una tarea está atrasada si sigue pendiente y su fecha ya pasó
        val atrasadas = pendientes.count { it.fechaEntrega < hoy }

        // Las próximas entregas son las pendientes que aún no vencen, de la más cercana a la más lejana
        val proximas = pendientes
            .filter { it.fechaEntrega >= hoy }
            .sortedBy { it.fechaEntrega }
            .take(MAXIMO_ENTREGAS)
            .map { tarea ->
                val dias = ((tarea.fechaEntrega - hoy) / MILISEGUNDOS_POR_DIA).toInt()
                EntregaUi(
                    titulo = tarea.titulo,
                    materia = materias.firstOrNull { it.id == tarea.materiaId }?.nombre ?: "",
                    vence = textoVencimiento(dias),
                    prioridad = tarea.prioridad
                )
            }

        // El avance de cada materia activa
        val rendimiento = materias
            .filter { !it.archivada }
            .map { materia ->
                val deLaMateria = tareas.filter { it.materiaId == materia.id }
                MateriaRendimientoUi(
                    nombre = materia.nombre,
                    avance = calcularPorcentaje(deLaMateria.count { it.completada }, deLaMateria.size),
                    color = materia.color.aColor()
                )
            }

        return InicioUiState(
            fechaHoy = fechaDeHoyTexto(),
            pendientes = pendientes.size,
            atrasadas = atrasadas,
            completadas = completadas,
            cumplimiento = calcularPorcentaje(completadas, tareas.size),
            proximasEntregas = proximas,
            rendimientoMaterias = rendimiento
        )
    }
}