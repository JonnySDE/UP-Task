package com.cursokotlin.uptask.task.presentations.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cursokotlin.uptask.R
import com.cursokotlin.uptask.task.presentations.components.BarraBusqueda
import com.cursokotlin.uptask.task.presentations.components.BotonAgregar
import com.cursokotlin.uptask.task.presentations.components.ChipFiltro
import com.cursokotlin.uptask.task.presentations.components.ItemTarea
import com.cursokotlin.uptask.task.presentations.components.SelectorOrden
import com.cursokotlin.uptask.task.presentations.viewmodels.TareasViewModel


@Composable
fun TareasPage(
    alAgregarTarea: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TareasViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {
        TareasContenido(
            uiState = uiState,
            alCambiarBusqueda = viewModel::cambiarBusqueda,
            alCambiarFiltroMateria = viewModel::cambiarFiltroMateria,
            alCambiarFiltroEstado = viewModel::cambiarFiltroEstado,
            alCambiarFiltroPrioridad = viewModel::cambiarFiltroPrioridad,
            alCambiarOrden = viewModel::cambiarOrden,
            alCambiarEstadoTarea = viewModel::cambiarEstadoTarea,
            alEliminarTarea = viewModel::eliminarTarea
        )

        BotonAgregar(
            descripcion = stringResource(R.string.agregar_tarea),
            alPresionar = alAgregarTarea,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }
}

@Composable
fun TareasContenido(
    uiState: TareasUiState,
    alCambiarBusqueda: (String) -> Unit,
    alCambiarFiltroMateria: () -> Unit,
    alCambiarFiltroEstado: () -> Unit,
    alCambiarFiltroPrioridad: () -> Unit,
    alCambiarOrden: () -> Unit,
    alCambiarEstadoTarea: (Int, Boolean) -> Unit,
    alEliminarTarea: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Encabezado
        item {
            Column {
                Text(
                    text = stringResource(R.string.app_etiqueta),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = stringResource(R.string.tareas_titulo),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.tareas_subtitulo),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Buscador
        item {
            BarraBusqueda(
                texto = uiState.textoBusqueda,
                alCambiarTexto = alCambiarBusqueda
            )
        }

        // Filtro
        item {
            val textoMateria = uiState.filtroMateria ?: stringResource(R.string.filtro_todas)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ChipFiltro(
                    texto = stringResource(R.string.filtro_materia, textoMateria),
                    seleccionado = uiState.filtroMateria != null,
                    alPresionar = alCambiarFiltroMateria,
                    modifier = Modifier.weight(1f)
                )
                ChipFiltro(
                    texto = stringResource(
                        R.string.filtro_estado,
                        stringResource(uiState.filtroEstado.texto)
                    ),
                    seleccionado = uiState.filtroEstado != FiltroEstado.TODAS,
                    alPresionar = alCambiarFiltroEstado,
                    modifier = Modifier.weight(1f)
                )
                ChipFiltro(
                    texto = stringResource(
                        R.string.filtro_prioridad,
                        stringResource(uiState.filtroPrioridad.texto)
                    ),
                    seleccionado = uiState.filtroPrioridad != FiltroPrioridad.TODAS,
                    alPresionar = alCambiarFiltroPrioridad,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Selector de orden
        item {
            SelectorOrden(
                texto = stringResource(uiState.orden.texto),
                alPresionar = alCambiarOrden
            )
        }

        // Mensaje cuando no hay nada que mostrar
        if (uiState.tareasPendientes.isEmpty() && uiState.tareasEntregadas.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.tareas_sin_resultados),
                    modifier = Modifier.padding(top = 24.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Tareas pendientes
        if (uiState.tareasPendientes.isNotEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.tareas_pendientes_titulo, uiState.tareasPendientes.size),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            items(uiState.tareasPendientes, key = { it.id }) { tarea ->
                ItemTarea(
                    tarea = tarea,
                    alCambiarEstado = { completada -> alCambiarEstadoTarea(tarea.id, completada) },
                    alEditar = { },
                    alEliminar = { alEliminarTarea(tarea.id) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Tareas entregadas
        if (uiState.tareasEntregadas.isNotEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.tareas_entregadas_titulo, uiState.tareasEntregadas.size),
                    modifier = Modifier.padding(top = 12.dp),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            items(uiState.tareasEntregadas, key = { it.id }) { tarea ->
                ItemTarea(
                    tarea = tarea,
                    alCambiarEstado = { completada -> alCambiarEstadoTarea(tarea.id, completada) },
                    alEditar = {},
                    alEliminar = { alEliminarTarea(tarea.id) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}