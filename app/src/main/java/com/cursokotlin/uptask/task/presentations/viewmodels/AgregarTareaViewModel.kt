package com.cursokotlin.uptask.task.presentations.viewmodels

import androidx.lifecycle.ViewModel
import com.cursokotlin.uptask.task.presentations.screens.AgregarTareaUiState
import com.cursokotlin.uptask.task.presentations.screens.MateriaUi
import com.cursokotlin.uptask.task.presentations.screens.NuevaTarea
import com.cursokotlin.uptask.task.presentations.screens.Prioridad
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

    // Las 7 materias de prueba.
    private val materiasDePrueba = listOf(
        MateriaUi(1, "Aplicaciones Móviles", VerdeMateria),
        MateriaUi(2, "Aplicaciones Web", AzulMateria),
        MateriaUi(3, "Análisis y Diseño", MoradoMateria),
        MateriaUi(4, "Inglés IV", NaranjaMateria),
        MateriaUi(5, "Ética Profesional", AzulMarinoMateria),
        MateriaUi(6, "Estructura de Datos", GrisMateria),
        MateriaUi(7, "Cálculo Varias Variables", RosaMateria)
    )

    init {
        reiniciar()
    }
    // Deja el formulario como nuevo: vacío, con la primera materia y la fecha de hoy
    fun reiniciar() {
        val hoy = hoyEnMillisUtc()
        _uiState.value = AgregarTareaUiState(
            materias = materiasDePrueba,
            materiaSeleccionada = materiasDePrueba.first(),
            fechaMillis = hoy,
            fechaTexto = formatearFecha(hoy)
        )
    }


    fun cambiarTitulo(texto: String) {
        // Al escribir, el mensaje de error desaparece
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

    // Valida el formulario. Si está bien, entrega la tarea y limpia el formulario.
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
                fechaMillis = estado.fechaMillis
            )
        )
        reiniciar()
    }
}