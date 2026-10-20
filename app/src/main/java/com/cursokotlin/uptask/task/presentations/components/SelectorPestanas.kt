package com.cursokotlin.uptask.task.presentations.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cursokotlin.uptask.ui.theme.GrisSelector

@Composable
fun SelectorPestanas(
    opciones: List<String>,
    seleccionada: Int,
    alSeleccionar: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(GrisSelector, RoundedCornerShape(50))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        // Un botón por cada opción. La activa se pinta blanca con letras rojas.
        opciones.forEachIndexed { posicion, texto ->
            val activa = posicion == seleccionada

            Surface(
                onClick = { alSeleccionar(posicion) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(50),
                color = if (activa) MaterialTheme.colorScheme.surface else Color.Transparent,
                shadowElevation = if (activa) 1.dp else 0.dp
            ) {
                Text(
                    text = texto,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (activa) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}