package com.cursokotlin.uptask.task.presentations.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cursokotlin.uptask.task.data.repository.UpTaskRepository
import com.cursokotlin.uptask.task.presentations.screens.AgregarMateriaUiState
import com.cursokotlin.uptask.task.presentations.screens.MateriaFormulario
import com.cursokotlin.uptask.task.presentations.screens.MateriaResumenUi
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
import kotlinx.coroutines.launch

class AgregarMateriaViewModel(
    private val repository: UpTaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AgregarMateriaUiState())
    val uiState: StateFlow<AgregarMateriaUiState> = _uiState.asStateFlow()

    // Los colores que el usuario puede elegir
    private val coloresDisponibles: List<Color> = listOf(
        VerdeMateria,
        AzulMateria,
        MoradoMateria,
        AzulMarinoMateria,
        GrisMateria,
        NaranjaMateria,
        RosaMateria
    )

    init {
        iniciarNueva()
    }

    // ---------- Cómo se abre el formulario ----------

    // Formulario vacío para crear una materia
    fun iniciarNueva() {
        _uiState.value = AgregarMateriaUiState(
            colores = coloresDisponibles,
            colorSeleccionado = coloresDisponibles.first()
        )
    }

    // Formulario con los datos de una materia que ya existe
    fun iniciarEdicion(materia: MateriaResumenUi) {
        _uiState.value = AgregarMateriaUiState(
            id = materia.id,
            nombre = materia.nombre,
            colores = coloresDisponibles,
            colorSeleccionado = materia.color,
            imagenUri = materia.imagenUri,
            archivada = materia.archivada
        )
    }

    // ---------- EVENTOS del formulario ----------

    fun cambiarNombre(texto: String) {
        // Al escribir, el mensaje de error desaparece
        _uiState.update { estado -> estado.copy(nombre = texto, errorNombre = false) }
    }

    fun cambiarColor(color: Color) {
        _uiState.update { estado -> estado.copy(colorSeleccionado = color) }
    }

    // La imagen se copia a la memoria de la app antes de guardarla en el estado
    fun cambiarImagen(uri: String) {
        viewModelScope.launch {
            val ruta = repository.guardarImagen(uri)
            if (ruta != null) {
                _uiState.update { estado -> estado.copy(imagenUri = ruta) }
            }
        }
    }

    fun cambiarArchivada(archivada: Boolean) {
        _uiState.update { estado -> estado.copy(archivada = archivada) }
    }

    // Valida el formulario. Si está bien, entrega los datos.
    fun guardar(alGuardar: (MateriaFormulario) -> Unit) {
        val estado = _uiState.value

        if (estado.nombre.isBlank()) {
            _uiState.update { it.copy(errorNombre = true) }
            return
        }

        alGuardar(
            MateriaFormulario(
                id = estado.id,
                nombre = estado.nombre.trim(),
                color = estado.colorSeleccionado,
                imagenUri = estado.imagenUri,
                archivada = estado.archivada
            )
        )
    }
}