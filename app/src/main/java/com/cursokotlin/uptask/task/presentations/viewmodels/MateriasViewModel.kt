package com.cursokotlin.uptask.task.presentations.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.cursokotlin.uptask.task.presentations.screens.MateriaResumenUi
import com.cursokotlin.uptask.task.presentations.screens.MateriasUiState
import com.cursokotlin.uptask.ui.theme.AzulMarinoMateria
import com.cursokotlin.uptask.ui.theme.AzulMateria
import com.cursokotlin.uptask.ui.theme.GrisMateria
import com.cursokotlin.uptask.ui.theme.MoradoMateria
import com.cursokotlin.uptask.ui.theme.NaranjaMateria
import com.cursokotlin.uptask.ui.theme.RosaMateria
import com.cursokotlin.uptask.ui.theme.VerdeMateria
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.roundToInt
import com.cursokotlin.uptask.task.presentations.screens.MateriaFormulario

class MateriasViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MateriasUiState())

    val uiState: StateFlow<MateriasUiState> = _uiState.asStateFlow()

    private var todasLasMaterias: List<MateriaResumenUi> = emptyList()

    init {
        cargarDatosDePrueba()
    }


    // Cambia entre la pestaña Activas y la pestaña Archivadas
    fun cambiarPestana(archivadas: Boolean) {
        _uiState.update { estadoActual -> estadoActual.copy(mostrandoArchivadas = archivadas) }
        mostrarMaterias()
    }

    // Archiva una materia activa o restaura una archivada
    fun alternarArchivada(id: Int) {
        todasLasMaterias = todasLasMaterias.map { materia ->
            if (materia.id == id) materia.copy(archivada = !materia.archivada) else materia
        }
        mostrarMaterias()
    }

    // Recibe lo que el formulario guardó: crea una materia nueva o actualiza una existente
    fun guardarMateria(datos: MateriaFormulario) {
        if (datos.id == null) {
            // Materia nueva: empieza sin tareas
            val nueva = crearMateria(
                id = (todasLasMaterias.maxOfOrNull { it.id } ?: 0) + 1,
                nombre = datos.nombre,
                color = datos.color,
                pendientes = 0,
                completadas = 0
            ).copy(imagenUri = datos.imagenUri, archivada = datos.archivada)

            todasLasMaterias = todasLasMaterias + nueva
        } else {
            // Materia existente: solo cambian los datos del formulario
            todasLasMaterias = todasLasMaterias.map { materia ->
                if (materia.id == datos.id) {
                    materia.copy(
                        nombre = datos.nombre,
                        color = datos.color,
                        imagenUri = datos.imagenUri,
                        archivada = datos.archivada
                    )
                } else {
                    materia
                }
            }
        }
        mostrarMaterias()
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

    // Porcentaje = completadas / total de tareas. Si no hay tareas, es 0.
    private fun calcularCumplimiento(pendientes: Int, completadas: Int): Int {
        val total = pendientes + completadas
        if (total == 0) return 0
        return (completadas * 100f / total).roundToInt()
    }

    private fun crearMateria(
        id: Int,
        nombre: String,
        color: Color,
        pendientes: Int,
        completadas: Int
    ): MateriaResumenUi {
        return MateriaResumenUi(
            id = id,
            nombre = nombre,
            color = color,
            pendientes = pendientes,
            completadas = completadas,
            cumplimiento = calcularCumplimiento(pendientes, completadas),
            archivada = false
        )
    }

    // Datos temporales
    private fun cargarDatosDePrueba() {
        todasLasMaterias = listOf(
            crearMateria(1, "Apps Móviles", VerdeMateria, pendientes = 2, completadas = 5),
            crearMateria(2, "Apps Web", AzulMateria, pendientes = 1, completadas = 3),
            crearMateria(3, "Análisis y Diseño", MoradoMateria, pendientes = 3, completadas = 2),
            crearMateria(4, "Inglés IV", NaranjaMateria, pendientes = 2, completadas = 2),
            crearMateria(5, "Ética Profesional", AzulMarinoMateria, pendientes = 0, completadas = 0),
            crearMateria(6, "Estructura de Datos", GrisMateria, pendientes = 4, completadas = 1),
            crearMateria(7, "Cálculo Varias Variables", RosaMateria, pendientes = 3, completadas = 0)
        )
        mostrarMaterias()
    }
}