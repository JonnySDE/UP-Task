package com.cursokotlin.uptask.task.presentations.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cursokotlin.uptask.R

@Composable
fun TarjetaMateria(
    nombre: String,
    avance: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .background(color.copy(alpha = 0.15f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = nombre.take(1), //Para mostrar la primera letra de  la palabra luego se cambiara a imagen
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
            Text(
                text = nombre,
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.inicio_avance, avance),
                style = MaterialTheme.typography.labelSmall,
                color = color
            )
            LinearProgressIndicator(
                progress = { avance / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                color = color,
                trackColor = Color(0xFFE5E5E5),
                drawStopIndicator = {}
            )
        }
    }
}