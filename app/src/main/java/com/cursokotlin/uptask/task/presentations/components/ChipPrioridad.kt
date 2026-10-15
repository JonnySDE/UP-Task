package com.cursokotlin.uptask.task.presentations.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cursokotlin.uptask.R
import com.cursokotlin.uptask.task.presentations.screens.Prioridad
import com.cursokotlin.uptask.ui.theme.PrioridadBajaFondo
import com.cursokotlin.uptask.ui.theme.PrioridadBajaTexto
import com.cursokotlin.uptask.ui.theme.PrioridadMediaFondo
import com.cursokotlin.uptask.ui.theme.PrioridadMediaTexto

@Composable
fun ChipPrioridad(
    prioridad: Prioridad,
    modifier: Modifier = Modifier
) {
    // Según la prioridad cambia el texto y los colores
    val texto = when (prioridad) {
        Prioridad.ALTA -> stringResource(R.string.prioridad_alta)
        Prioridad.MEDIA -> stringResource(R.string.prioridad_media)
        Prioridad.BAJA -> stringResource(R.string.prioridad_baja)
    }
    val colorFondo = when (prioridad) {
        Prioridad.ALTA -> MaterialTheme.colorScheme.primary
        Prioridad.MEDIA -> PrioridadMediaFondo
        Prioridad.BAJA -> PrioridadBajaFondo
    }
    val colorTexto = when (prioridad) {
        Prioridad.ALTA -> Color.White
        Prioridad.MEDIA -> PrioridadMediaTexto
        Prioridad.BAJA -> PrioridadBajaTexto
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = colorFondo
    ) {
        Text(
            text = texto,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = colorTexto
        )
    }
}