package com.cursokotlin.uptask.task.presentations.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cursokotlin.uptask.task.data.model.Materia
import com.cursokotlin.uptask.task.data.model.Tarea
import com.cursokotlin.uptask.task.data.repository.UpTaskRepository
import com.cursokotlin.uptask.task.presentations.screens.MateriaFormulario
import com.cursokotlin.uptask.task.presentations.screens.MateriaResumenUi
import com.cursokotlin.uptask.task.presentations.screens.MateriasUiState
import com.cursokotlin.uptask.task.presentations.utils.aColor
import com.cursokotlin.uptask.task.presentations.utils.aHex
import com.cursokotlin.uptask.task.presentations.utils.calcularPorcentaje
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MateriasViewModel(
    private val repository: UpTaskRepository
) : ViewModel() {

    // _uiState es privado: solo el ViewModel lo puede cambiar
    private val _uiState = MutableStateFlow(MateriasUiState())

    // uiState es público y de solo lectura: la página solo lo observa
    val uiState: StateFlow<MateriasUiState> = _uiState.asStateFlow()

    // La lista COMPLETA de materias (activas y archivadas). Solo el ViewModel la conoce.
    private var todasLasMaterias: List<MateriaResumenUi> = emptyList()

    init {
        observarDatos()
    }

    // Escucha al Repository. Cuando cambian las materias o las tareas, se recalcula todo.
    private fun observarDatos() {
        viewModelScope.launch {
            combine(
                repository.obtenerMaterias(),
                repository.obtenerTareas()
            ) { materias, tareas ->
                convertirAMateriasUi(materias, tareas)
            }.collect { materiasUi ->
                todasLasMaterias = materiasUi
                mostrarMaterias()
            }
        }
    }

    // Cuenta las tareas de cada materia y calcula su porcentaje de cumplimiento
    private fun convertirAMateriasUi(materias: List<Materia>, tareas: List<Tarea>): List<MateriaResumenUi> {
        return materias.map { materia ->
            val tareasDeLaMateria = tareas.filter { it.materiaId == materia.id }
            val completadas = tareasDeLaMateria.count { it.completada }

            MateriaResumenUi(
                id = materia.id,
                nombre = materia.nombre,
                color = materia.color.aColor(),
                pendientes = tareasDeLaMateria.size - completadas,
                completadas = completadas,
                cumplimiento = calcularPorcentaje(completadas, tareasDeLaMateria.size),
                archivada = materia.archivada,
                imagenUri = materia.imagenUri
            )
        }
    }

    // Cambia entre la pestaña Activas y la pestaña Archivadas
    fun cambiarPestana(archivadas: Boolean) {
        _uiState.update { estadoActual -> estadoActual.copy(mostrandoArchivadas = archivadas) }
        mostrarMaterias()
    }

    // Archiva una materia activa o restaura una archivada
    fun alternarArchivada(id: Int) {
        viewModelScope.launch {
            repository.alternarArchivada(id)
        }
    }

    // Recibe lo que el formulario guardó. Si trae id es una edición, si no, es una materia nueva.
    fun guardarMateria(datos: MateriaFormulario) {
        viewModelScope.launch {
            repository.guardarMateria(
                Materia(
                    id = datos.id ?: 0,
                    nombre = datos.nombre,
                    color = datos.color.aHex(),
                    imagenUri = datos.imagenUri,
                    archivada = datos.archivada
                )
            )
        }
    }

    // Separa activas y archivadas, y actualiza lo que se ve en pantalla
    private fun mostrarMaterias() {
        val activas = todasLasMaterias.filter { materia -> !materia.archivada }
        val archivadas = todasLasMaterias.filter { materia -> materia.archivada }

        _uiState.update { estadoActual ->
            estadoActual.copy(
                totalActivas = activas.size,
                totalArchivadas = archivadas.size,
                materiasMostradas = if (estadoActual.mostrandoArchivadas) archivadas else activas
            )
        }
    }
}