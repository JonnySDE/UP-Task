package com.cursokotlin.uptask.task.presentations.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cursokotlin.uptask.R
import com.cursokotlin.uptask.task.data.model.Prioridad
import com.cursokotlin.uptask.ui.theme.GrisSelector

@Composable
fun SelectorPrioridad(
    etiqueta: String,
    seleccionada: Prioridad,
    alSeleccionar: (Prioridad) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp)
                .background(GrisSelector, RoundedCornerShape(50))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Un botón por cada prioridad: BAJA, MEDIA y ALTA
            Prioridad.entries.forEach { prioridad ->
                val activa = prioridad == seleccionada

                // El texto de cada prioridad sale de strings.xml
                val textoPrioridad = when (prioridad) {
                    Prioridad.BAJA -> stringResource(R.string.prioridad_baja)
                    Prioridad.MEDIA -> stringResource(R.string.prioridad_media)
                    Prioridad.ALTA -> stringResource(R.string.prioridad_alta)
                }

                Surface(
                    onClick = { alSeleccionar(prioridad) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(50),
                    color = if (activa) MaterialTheme.colorScheme.primary else Color.Transparent
                ) {
                    Text(
                        text = textoPrioridad.uppercase(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (activa) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}