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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cursokotlin.uptask.R

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
    // Guarda en qué sección estamos. Empezamos en la vista de Inicio
    var seccionActual by rememberSaveable { mutableStateOf(Seccion.INICIO) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                // Dibuja un botón por cada sección
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
        // Muestra la página que corresponde a la sección actual
        when (seccionActual) {
            Seccion.INICIO -> InicioPage(
                alAgregarTarea = { },
                alVerTodas = { seccionActual = Seccion.TAREAS },
                modifier = Modifier.padding(espacioSeguro)
            )
            Seccion.TAREAS -> TareasPage(
                alAgregarTarea = { },
                modifier = Modifier.padding(espacioSeguro)
            )
            Seccion.MATERIAS -> MateriasPage(modifier = Modifier.padding(espacioSeguro))
        }
    }
}