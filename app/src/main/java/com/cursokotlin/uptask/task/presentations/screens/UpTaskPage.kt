package com.cursokotlin.uptask.task.presentations.screens

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cursokotlin.uptask.R
import com.cursokotlin.uptask.task.di.UpTaskModule
import com.cursokotlin.uptask.task.presentations.viewmodels.AgregarMateriaViewModel
import com.cursokotlin.uptask.task.presentations.viewmodels.AgregarTareaViewModel
import com.cursokotlin.uptask.task.presentations.viewmodels.InicioViewModel
import com.cursokotlin.uptask.task.presentations.viewmodels.MateriasViewModel
import com.cursokotlin.uptask.task.presentations.viewmodels.TareasViewModel

// Las tres secciones de la barra, cada una con su texto y su ícono
enum class Seccion(
    @StringRes val titulo: Int,
    @DrawableRes val icono: Int
) {
    INICIO(R.string.nav_inicio, R.drawable.ic_inicio),
    TAREAS(R.string.nav_tareas, R.drawable.ic_tareas),
    MATERIAS(R.string.nav_materias, R.drawable.ic_materias)
}

@Composable
fun UpTaskPage() {
    // En qué sección de la barra estamos
    var seccionActual by rememberSaveable { mutableStateOf(Seccion.INICIO) }

    // Si alguno es true, se muestra el formulario en lugar de la barra
    var mostrandoFormularioTarea by rememberSaveable { mutableStateOf(false) }
    var mostrandoFormularioMateria by rememberSaveable { mutableStateOf(false) }

    // La Factory sabe armar los ViewModels que necesitan el Repository
    val factory = UpTaskModule.proveerFactory(LocalContext.current)

    val inicioViewModel: InicioViewModel = viewModel(factory = factory)
    val tareasViewModel: TareasViewModel = viewModel(factory = factory)
    val materiasViewModel: MateriasViewModel = viewModel(factory = factory)
    val formularioTareaViewModel: AgregarTareaViewModel = viewModel(factory = factory)
    val formularioMateriaViewModel: AgregarMateriaViewModel = viewModel(factory = factory)

    // Abre el formulario de tarea vacío. Lo usan Inicio y Tareas.
    val abrirNuevaTarea = {
        formularioTareaViewModel.iniciarNueva()
        mostrandoFormularioTarea = true
    }

    when {
        mostrandoFormularioTarea -> {
            AgregarTareaPage(
                alVolver = { mostrandoFormularioTarea = false },
                alGuardar = { datos -> tareasViewModel.guardarTarea(datos) },
                viewModel = formularioTareaViewModel
            )
        }

        mostrandoFormularioMateria -> {
            AgregarMateriaPage(
                alVolver = { mostrandoFormularioMateria = false },
                alGuardar = { datos -> materiasViewModel.guardarMateria(datos) },
                viewModel = formularioMateriaViewModel
            )
        }

        else -> {
            Scaffold(
                containerColor = MaterialTheme.colorScheme.background,
                bottomBar = {
                    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                        Seccion.entries.forEach { seccion ->
                            NavigationBarItem(
                                selected = seccion == seccionActual,
                                onClick = { seccionActual = seccion },
                                icon = {
                                    Icon(
                                        painter = painterResource(seccion.icono),
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp)
                                    )
                                },
                                label = { Text(text = stringResource(seccion.titulo)) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.primary,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        }
                    }
                }
            ) { espacioSeguro ->
                when (seccionActual) {
                    Seccion.INICIO -> InicioPage(
                        alAgregarTarea = abrirNuevaTarea,
                        alVerTodas = { seccionActual = Seccion.TAREAS },
                        modifier = Modifier.padding(espacioSeguro),
                        viewModel = inicioViewModel
                    )

                    Seccion.TAREAS -> TareasPage(
                        alAgregarTarea = abrirNuevaTarea,
                        alEditarTarea = { tarea ->
                            // Antes de abrir, el formulario se llena con la tarea elegida
                            formularioTareaViewModel.iniciarEdicion(tarea)
                            mostrandoFormularioTarea = true
                        },
                        modifier = Modifier.padding(espacioSeguro),
                        viewModel = tareasViewModel
                    )

                    Seccion.MATERIAS -> MateriasPage(
                        alAgregarMateria = {
                            // Antes de abrir, el formulario se deja vacío
                            formularioMateriaViewModel.iniciarNueva()
                            mostrandoFormularioMateria = true
                        },
                        alEditarMateria = { materia ->
                            // Antes de abrir, el formulario se llena con la materia elegida
                            formularioMateriaViewModel.iniciarEdicion(materia)
                            mostrandoFormularioMateria = true
                        },
                        modifier = Modifier.padding(espacioSeguro),
                        viewModel = materiasViewModel
                    )
                }
            }
        }
    }
}