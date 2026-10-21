package com.cursokotlin.uptask.task.presentations.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cursokotlin.uptask.task.data.model.Materia
import com.cursokotlin.uptask.task.data.model.Prioridad
import com.cursokotlin.uptask.task.data.repository.UpTaskRepository
import com.cursokotlin.uptask.task.presentations.screens.AgregarTareaUiState
import com.cursokotlin.uptask.task.presentations.screens.MateriaUi
import com.cursokotlin.uptask.task.presentations.screens.NuevaTarea
import com.cursokotlin.uptask.task.presentations.screens.TareaUi
import com.cursokotlin.uptask.task.presentations.utils.aColor
import com.cursokotlin.uptask.task.presentations.utils.MILISEGUNDOS_POR_DIA
import com.cursokotlin.uptask.task.presentations.utils.formatearFecha
import com.cursokotlin.uptask.task.presentations.utils.hoyEnMillisUtc
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AgregarTareaViewModel(
    private val repository: UpTaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AgregarTareaUiState())
    val uiState: StateFlow<AgregarTareaUiState> = _uiState.asStateFlow()

    // Las materias reales que vienen del Repository
    private var todasLasMaterias: List<Materia> = emptyList()

    // Si se está editando una tarea, su materia siempre debe poder elegirse
    private var materiaEnEdicionId: Int? = null

    init {
        iniciarNueva()
        observarMaterias()
    }

    // Escucha al Repository: si se crea o archiva una materia, el menú se actualiza solo
    private fun observarMaterias() {
        viewModelScope.launch {
            repository.obtenerMaterias().collect { materias ->
                todasLasMaterias = materias
                actualizarMaterias()
            }
        }
    }

    // Solo se pueden elegir materias activas (o la de la tarea que se está editando)
    private fun materiasParaElegir(): List<MateriaUi> {
        return todasLasMaterias
            .filter { materia -> !materia.archivada || materia.id == materiaEnEdicionId }
            .map { materia -> MateriaUi(materia.id, materia.nombre, materia.color.aColor()) }
    }

    private fun actualizarMaterias() {
        val lista = materiasParaElegir()
        _uiState.update { estado ->
            val seleccionada = lista.firstOrNull { it.id == estado.materiaSeleccionada?.id }
                ?: lista.firstOrNull()
            estado.copy(materias = lista, materiaSeleccionada = seleccionada)
        }
    }

    // ---------- Cómo se abre el formulario ----------

    // Formulario vacío, con la primera materia y la fecha de hoy
    fun iniciarNueva() {
        materiaEnEdicionId = null
        val hoy = hoyEnMillisUtc()
        val lista = materiasParaElegir()

        _uiState.value = AgregarTareaUiState(
            materias = lista,
            materiaSeleccionada = lista.firstOrNull(),
            fechaMillis = hoy,
            fechaTexto = formatearFecha(hoy)
        )
    }

    // Formulario con los datos de una tarea que ya existe
    fun iniciarEdicion(tarea: TareaUi) {
        materiaEnEdicionId = tarea.materiaId

        // La fecha se reconstruye a partir de los días que faltan para vencer
        val fecha = hoyEnMillisUtc() + tarea.diasParaVencer * MILISEGUNDOS_POR_DIA
        val lista = materiasParaElegir()

        _uiState.value = AgregarTareaUiState(
            id = tarea.id,
            titulo = tarea.titulo,
            descripcion = tarea.descripcion,
            materias = lista,
            materiaSeleccionada = lista.firstOrNull { it.id == tarea.materiaId } ?: lista.firstOrNull(),
            fechaMillis = fecha,
            fechaTexto = formatearFecha(fecha),
            prioridad = tarea.prioridad
        )
    }

    // ---------- EVENTOS del formulario ----------

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

    // Valida el formulario. Si está bien, entrega los datos.
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