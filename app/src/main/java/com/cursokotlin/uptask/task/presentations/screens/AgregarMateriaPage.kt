package com.cursokotlin.uptask.task.presentations.screens

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cursokotlin.uptask.R
import com.cursokotlin.uptask.task.presentations.components.CampoTexto
import com.cursokotlin.uptask.task.presentations.components.SelectorColor
import com.cursokotlin.uptask.task.presentations.components.SelectorImagen
import com.cursokotlin.uptask.task.presentations.components.TarjetaInterruptor
import com.cursokotlin.uptask.task.presentations.viewmodels.AgregarMateriaViewModel

// Esta página se conecta con el ViewModel y observa su estado
@Composable
fun AgregarMateriaPage(
    alVolver: () -> Unit,
    alGuardar: (MateriaFormulario) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AgregarMateriaViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // El botón "atrás" del teléfono cierra el formulario
    BackHandler(onBack = alVolver)

    // Abre la galería de Android. Cuando el usuario elige una foto, se la pasamos al ViewModel.
    val lanzadorGaleria = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) viewModel.cambiarImagen(uri.toString())
    }

    AgregarMateriaContenido(
        uiState = uiState,
        alCambiarNombre = viewModel::cambiarNombre,
        alElegirImagen = {
            lanzadorGaleria.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        },
        alCambiarColor = viewModel::cambiarColor,
        alCambiarArchivada = viewModel::cambiarArchivada,
        alGuardar = {
            viewModel.guardar { datos ->
                alGuardar(datos)
                alVolver()
            }
        },
        alCancelar = alVolver,
        modifier = modifier
    )
}

@Composable
fun AgregarMateriaContenido(
    uiState: AgregarMateriaUiState,
    alCambiarNombre: (String) -> Unit,
    alElegirImagen: () -> Unit,
    alCambiarColor: (Color) -> Unit,
    alCambiarArchivada: (Boolean) -> Unit,
    alGuardar: () -> Unit,
    alCancelar: () -> Unit,
    modifier: Modifier = Modifier
) {
    // El mismo formulario sirve para crear y para editar. Solo cambia el título.
    val titulo = if (uiState.esEdicion) R.string.editar_materia_titulo else R.string.nueva_materia_titulo

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
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    text = stringResource(titulo),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            CampoTexto(
                etiqueta = stringResource(R.string.campo_nombre_materia),
                valor = uiState.nombre,
                alCambiar = alCambiarNombre,
                placeholder = stringResource(R.string.campo_nombre_materia_placeholder),
                mensajeError = if (uiState.errorNombre) stringResource(R.string.error_nombre_materia) else null
            )

            SelectorImagen(
                etiqueta = stringResource(R.string.campo_imagen),
                imagenUri = uiState.imagenUri,
                alElegir = alElegirImagen
            )

            SelectorColor(
                etiqueta = stringResource(R.string.campo_color),
                colores = uiState.colores,
                seleccionado = uiState.colorSeleccionado,
                alSeleccionar = alCambiarColor
            )

            TarjetaInterruptor(
                titulo = stringResource(R.string.archivar_materia),
                descripcion = stringResource(R.string.archivar_materia_descripcion),
                activo = uiState.archivada,
                alCambiar = alCambiarArchivada
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
                    text = stringResource(R.string.guardar_materia),
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
    }
}