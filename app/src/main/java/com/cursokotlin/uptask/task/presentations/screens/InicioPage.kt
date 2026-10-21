package com.cursokotlin.uptask.task.presentations.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
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
import com.cursokotlin.uptask.task.presentations.components.BotonAgregar
import com.cursokotlin.uptask.task.presentations.components.ItemEntrega
import com.cursokotlin.uptask.task.presentations.components.TarjetaEstadistica
import com.cursokotlin.uptask.task.presentations.components.TarjetaMateria
import com.cursokotlin.uptask.task.presentations.viewmodels.InicioViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.heightIn

@Composable
fun InicioPage(
    alAgregarTarea: () -> Unit,
    alVerTodas: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InicioViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {
        InicioContenido(
            uiState = uiState,
            alVerTodas = alVerTodas
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
fun InicioContenido(
    uiState: InicioUiState,
    alVerTodas: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Encabezado con saludo y fecha
        item {
            Column {
                Text(
                    text = stringResource(R.string.app_etiqueta),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = stringResource(
                        R.string.inicio_saludo,
                        stringResource(R.string.usuario_nombre)
                    ),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = uiState.fechaHoy,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Las tres tarjetas de números
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TarjetaEstadistica(
                    numero = uiState.pendientes,
                    texto = stringResource(R.string.inicio_pendientes),
                    modifier = Modifier.weight(1f)
                )
                TarjetaEstadistica(
                    numero = uiState.atrasadas,
                    texto = stringResource(R.string.inicio_atrasadas),
                    modifier = Modifier.weight(1f),
                    resaltada = true
                )
                TarjetaEstadistica(
                    numero = uiState.completadas,
                    texto = stringResource(R.string.inicio_completadas),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Barra de cumplimiento general
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.inicio_cumplimiento),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.porcentaje, uiState.cumplimiento),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    LinearProgressIndicator(
                        progress = { uiState.cumplimiento / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                }
            }
        }

        // Título "Próximas Entregas"
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.inicio_proximas),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.inicio_ver_todas),
                    modifier = Modifier.clickable(onClick = alVerTodas),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Lista de entregas
        item {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 300.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.proximasEntregas) { entrega ->
                    ItemEntrega(
                        entrega = entrega,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        // Título "Rendimiento por Materia"
        item {
            Text(
                text = stringResource(R.string.inicio_rendimiento),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        // Tarjetas que se deslizan hacia los lados
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(uiState.rendimientoMaterias) { materia ->
                    TarjetaMateria(
                        nombre = materia.nombre,
                        avance = materia.avance,
                        color = materia.color,
                        modifier = Modifier.width(150.dp)
                    )
                }
            }
        }
    }
}
