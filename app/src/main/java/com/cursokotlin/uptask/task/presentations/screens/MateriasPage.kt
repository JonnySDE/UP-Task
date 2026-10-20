package com.cursokotlin.uptask.task.presentations.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import com.cursokotlin.uptask.task.presentations.components.BotonAgregar
import com.cursokotlin.uptask.task.presentations.components.ItemMateria
import com.cursokotlin.uptask.task.presentations.components.SelectorPestanas
import com.cursokotlin.uptask.task.presentations.viewmodels.MateriasViewModel

// Esta página se conecta con el ViewModel y observa su estado
@Composable
fun MateriasPage(
    alAgregarMateria: () -> Unit,
    alEditarMateria: (MateriaResumenUi) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MateriasViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {
        MateriasContenido(
            uiState = uiState,
            alAgregarMateria = alAgregarMateria,
            alEditarMateria = alEditarMateria,
            alCambiarPestana = viewModel::cambiarPestana,
            alCambiarArchivo = viewModel::alternarArchivada
        )

        BotonAgregar(
            descripcion = stringResource(R.string.agregar_materia),
            alPresionar = alAgregarMateria,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }
}

// Esta función solo dibuja lo que recibe. No sabe que existe el ViewModel.
@Composable
fun MateriasContenido(
    uiState: MateriasUiState,
    alAgregarMateria: () -> Unit,
    alEditarMateria: (MateriaResumenUi) -> Unit,
    alCambiarPestana: (Boolean) -> Unit,
    alCambiarArchivo: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Encabezado con el botón (+) pequeño
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.app_etiqueta),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = stringResource(R.string.materias_titulo),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
                FilledIconButton(
                    onClick = alAgregarMateria,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_agregar),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Pestañas Activas / Archivadas
        item {
            SelectorPestanas(
                opciones = listOf(
                    stringResource(R.string.materias_activas, uiState.totalActivas),
                    stringResource(R.string.materias_archivadas, uiState.totalArchivadas)
                ),
                seleccionada = if (uiState.mostrandoArchivadas) 1 else 0,
                alSeleccionar = { posicion -> alCambiarPestana(posicion == 1) }
            )
        }

        // Mensaje cuando la lista está vacía
        if (uiState.materiasMostradas.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.materias_vacias),
                    modifier = Modifier.padding(top = 24.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Tarjetas de materias
        items(uiState.materiasMostradas, key = { it.id }) { materia ->
            ItemMateria(
                materia = materia,
                alEditar = { alEditarMateria(materia) },
                alCambiarArchivo = { alCambiarArchivo(materia.id) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}