package com.cursokotlin.uptask.task.presentations.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cursokotlin.uptask.R
import com.cursokotlin.uptask.task.presentations.components.CampoTexto
import com.cursokotlin.uptask.task.presentations.components.DialogoCalendario
import com.cursokotlin.uptask.task.presentations.components.SelectorFecha
import com.cursokotlin.uptask.task.presentations.components.SelectorMateria
import com.cursokotlin.uptask.task.presentations.components.SelectorPrioridad
import com.cursokotlin.uptask.task.presentations.viewmodels.AgregarTareaViewModel

@Composable
fun AgregarTareaPage(
    alVolver: () -> Unit,
    alGuardar: (NuevaTarea) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AgregarTareaViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler(onBack = alVolver)

    AgregarTareaContenido(
        uiState = uiState,
        alCambiarTitulo = viewModel::cambiarTitulo,
        alCambiarDescripcion = viewModel::cambiarDescripcion,
        alAbrirMenuMateria = viewModel::abrirMenuMateria,
        alCerrarMenuMateria = viewModel::cerrarMenuMateria,
        alSeleccionarMateria = viewModel::seleccionarMateria,
        alAbrirCalendario = viewModel::abrirCalendario,
        alCerrarCalendario = viewModel::cerrarCalendario,
        alSeleccionarFecha = viewModel::seleccionarFecha,
        alCambiarPrioridad = viewModel::cambiarPrioridad,
        alGuardar = {
            viewModel.guardar { nuevaTarea ->
                alGuardar(nuevaTarea)
                alVolver()
            }
        },
        alCancelar = alVolver,
        modifier = modifier
    )
}

@Composable
fun AgregarTareaContenido(
    uiState: AgregarTareaUiState,
    alCambiarTitulo: (String) -> Unit,
    alCambiarDescripcion: (String) -> Unit,
    alAbrirMenuMateria: () -> Unit,
    alCerrarMenuMateria: () -> Unit,
    alSeleccionarMateria: (MateriaUi) -> Unit,
    alAbrirCalendario: () -> Unit,
    alCerrarCalendario: () -> Unit,
    alSeleccionarFecha: (Long) -> Unit,
    alCambiarPrioridad: (Prioridad) -> Unit,
    alGuardar: () -> Unit,
    alCancelar: () -> Unit,
    modifier: Modifier = Modifier
) {

    val titulo = if (uiState.esEdicion) R.string.editar_tarea_titulo else R.string.nueva_tarea_titulo

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface
    ) { espacioSeguro ->
        Column(
            modifier = Modifier
                .padding(espacioSeguro)
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = alCancelar) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_regresar),
                        contentDescription = stringResource(R.string.volver),
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = stringResource(titulo),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            CampoTexto(
                etiqueta = stringResource(R.string.campo_titulo_tarea),
                valor = uiState.titulo,
                alCambiar = alCambiarTitulo,
                placeholder = stringResource(R.string.campo_titulo_placeholder),
                mensajeError = if (uiState.errorTitulo) stringResource(R.string.error_titulo) else null
            )

            CampoTexto(
                etiqueta = stringResource(R.string.campo_descripcion),
                valor = uiState.descripcion,
                alCambiar = alCambiarDescripcion,
                placeholder = stringResource(R.string.campo_descripcion_placeholder),
                lineasMinimas = 3
            )

            SelectorMateria(
                etiqueta = stringResource(R.string.campo_materia),
                materias = uiState.materias,
                seleccionada = uiState.materiaSeleccionada,
                expandido = uiState.menuMateriaAbierto,
                alAbrir = alAbrirMenuMateria,
                alCerrar = alCerrarMenuMateria,
                alSeleccionar = alSeleccionarMateria
            )

            SelectorFecha(
                etiqueta = stringResource(R.string.campo_fecha),
                fechaTexto = uiState.fechaTexto,
                alPresionar = alAbrirCalendario
            )

            SelectorPrioridad(
                etiqueta = stringResource(R.string.campo_prioridad),
                seleccionada = uiState.prioridad,
                alSeleccionar = alCambiarPrioridad
            )

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = alGuardar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    text = stringResource(R.string.guardar_tarea),
                    fontWeight = FontWeight.Bold
                )
            }

            OutlinedButton(
                onClick = alCancelar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Text(
                    text = stringResource(R.string.cancelar),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        if (uiState.calendarioVisible) {
            DialogoCalendario(
                fechaInicial = uiState.fechaMillis,
                alConfirmar = alSeleccionarFecha,
                alCancelar = alCerrarCalendario
            )
        }
    }
}