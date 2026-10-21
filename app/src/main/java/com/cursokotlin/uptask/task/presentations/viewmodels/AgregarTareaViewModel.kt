package com.cursokotlin.uptask.task.presentations.viewmodels

import androidx.lifecycle.ViewModel
import com.cursokotlin.uptask.task.presentations.screens.AgregarTareaUiState
import com.cursokotlin.uptask.task.presentations.screens.MateriaUi
import com.cursokotlin.uptask.task.presentations.screens.NuevaTarea
import com.cursokotlin.uptask.task.data.model.Prioridad
import com.cursokotlin.uptask.task.presentations.screens.TareaUi
import com.cursokotlin.uptask.task.presentations.utils.MILISEGUNDOS_POR_DIA
import com.cursokotlin.uptask.task.presentations.utils.formatearFecha
import com.cursokotlin.uptask.task.presentations.utils.hoyEnMillisUtc
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

class AgregarTareaViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AgregarTareaUiState())
    val uiState: StateFlow<AgregarTareaUiState> = _uiState.asStateFlow()

    private val materiasDePrueba = listOf(
        MateriaUi(1, "Apps Móviles", VerdeMateria),
        MateriaUi(2, "Apps Web", AzulMateria),
        MateriaUi(3, "Análisis y Diseño", MoradoMateria),
        MateriaUi(4, "Inglés IV", NaranjaMateria),
        MateriaUi(5, "Ética Profesional", AzulMarinoMateria),
        MateriaUi(6, "Estructura de Datos", GrisMateria),
        MateriaUi(7, "Cálculo Varias Variables", RosaMateria)
    )

    init {
        iniciarNueva()
    }

    fun iniciarNueva() {
        val hoy = hoyEnMillisUtc()
        _uiState.value = AgregarTareaUiState(
            materias = materiasDePrueba,
            materiaSeleccionada = materiasDePrueba.first(),
            fechaMillis = hoy,
            fechaTexto = formatearFecha(hoy)
        )
    }

    fun iniciarEdicion(tarea: TareaUi) {

        val fecha = hoyEnMillisUtc() + tarea.diasParaVencer * MILISEGUNDOS_POR_DIA

        val materia = materiasDePrueba.firstOrNull { it.nombre == tarea.materia }
            ?: materiasDePrueba.first()

        _uiState.value = AgregarTareaUiState(
            id = tarea.id,
            titulo = tarea.titulo,
            descripcion = tarea.descripcion,
            materias = materiasDePrueba,
            materiaSeleccionada = materia,
            fechaMillis = fecha,
            fechaTexto = formatearFecha(fecha),
            prioridad = tarea.prioridad
        )
    }

    fun cambiarTitulo(texto: String) {
        _uiState.update { estado -> estado.copy(titulo = texto, errorTitulo = false) }
    }

    fun cambiarDescripcion(texto: String) {
        _uiState.update { estado -> estado.copy(descripcion = texto) }
    }

    fun abrirMenuMateria() {
        _uiState.update { estado -> estado.copy(menuMateriaAbierto = true) }
    }

    fun cerrarMenuMateria() {
        _uiState.update { estado -> estado.copy(menuMateriaAbierto = false) }
    }

    fun seleccionarMateria(materia: MateriaUi) {
        _uiState.update { estado ->
            estado.copy(materiaSeleccionada = materia, menuMateriaAbierto = false)
        }
    }

    fun abrirCalendario() {
        _uiState.update { estado -> estado.copy(calendarioVisible = true) }
    }

    fun cerrarCalendario() {
        _uiState.update { estado -> estado.copy(calendarioVisible = false) }
    }

    fun seleccionarFecha(millis: Long) {
        _uiState.update { estado ->
            estado.copy(
                fechaMillis = millis,
                fechaTexto = formatearFecha(millis),
                calendarioVisible = false
            )
        }
    }

    fun cambiarPrioridad(prioridad: Prioridad) {
        _uiState.update { estado -> estado.copy(prioridad = prioridad) }
    }

    fun guardar(alGuardar: (NuevaTarea) -> Unit) {
        val estado = _uiState.value
        val materia = estado.materiaSeleccionada ?: return

        if (estado.titulo.isBlank()) {
            _uiState.update { it.copy(errorTitulo = true) }
            return
        }

        alGuardar(
            NuevaTarea(
                titulo = estado.titulo.trim(),
                descripcion = estado.descripcion.trim(),
                materia = materia,
                prioridad = estado.prioridad,
                fechaMillis = estado.fechaMillis,
                id = estado.id
            )
        )
    }
}