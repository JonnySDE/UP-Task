package com.cursokotlin.uptask.task.presentations.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cursokotlin.uptask.R
import com.cursokotlin.uptask.task.presentations.screens.MateriaUi

@Composable
fun SelectorMateria(
    etiqueta: String,
    materias: List<MateriaUi>,
    seleccionada: MateriaUi?,
    expandido: Boolean,
    alAbrir: () -> Unit,
    alCerrar: () -> Unit,
    alSeleccionar: (MateriaUi) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp)
        ) {
            // El cuadro que muestra la materia elegida
            Card(
                onClick = alAbrir,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (seleccionada != null) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(seleccionada.color, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                    }
                    Text(
                        text = seleccionada?.nombre ?: "",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_desplegar),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // La lista que se despliega
            DropdownMenu(
                expanded = expandido,
                onDismissRequest = alCerrar
            ) {
                materias.forEach { materia ->
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .background(materia.color, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(text = materia.nombre)
                            }
                        },
                        onClick = { alSeleccionar(materia) }
                    )
                }
            }
        }
    }
}