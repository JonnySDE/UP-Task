package com.cursokotlin.uptask.task.presentations.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TarjetaEstadistica(
    numero: Int,
    texto: String,
    modifier: Modifier = Modifier,
    resaltada: Boolean = false
) {
    // Si está resaltada se pinta de rojo con letras blancas
    val colorFondo = if (resaltada) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val colorTexto = if (resaltada) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = colorFondo),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = numero.toString(),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = colorTexto
            )
            Text(
                text = texto,
                style = MaterialTheme.typography.labelSmall,
                color = colorTexto
            )
        }
    }
}