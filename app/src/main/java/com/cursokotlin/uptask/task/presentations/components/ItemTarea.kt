package com.cursokotlin.uptask.task.presentations.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.cursokotlin.uptask.R
import com.cursokotlin.uptask.task.presentations.screens.TareaUi
import com.cursokotlin.uptask.ui.theme.VerdeMateria

@Composable
fun ItemTarea(
    tarea: TareaUi,
    alCambiarEstado: (Boolean) -> Unit,
    alEditar: () -> Unit,
    alEliminar: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Las tareas completadas se ven más claras y con el título tachado
    val transparencia = if (tarea.completada) 0.6f else 1f
    val decoracion = if (tarea.completada) TextDecoration.LineThrough else TextDecoration.None

    // Color del texto de abajo: verde si está completada, rojo si es urgente, gris normal
    val colorEstado = when {
        tarea.completada -> VerdeMateria
        tarea.urgente -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    val textoEstado = if (tarea.completada) {
        stringResource(R.string.tarea_completada)
    } else {
        stringResource(tarea.vence.recurso, tarea.vence.dias)
    }

    Card(
        modifier = modifier.alpha(transparencia),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            // Barra de color de la materia
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(tarea.colorMateria)
            )

            Checkbox(
                checked = tarea.completada,
                onCheckedChange = alCambiarEstado
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 12.dp)
            ) {
                Text(
                    text = tarea.titulo,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    textDecoration = decoracion
                )
                Row(
                    modifier = Modifier.padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ChipMateria(nombre = tarea.materia, color = tarea.colorMateria)
                    ChipPrioridad(prioridad = tarea.prioridad)
                }
                Text(
                    text = textoEstado,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = colorEstado
                )
            }

            Row(modifier = Modifier.padding(top = 4.dp, end = 4.dp)) {
                IconButton(onClick = alEditar, modifier = Modifier.size(32.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_editar),
                        contentDescription = stringResource(R.string.editar),
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = alEliminar, modifier = Modifier.size(32.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_eliminar),
                        contentDescription = stringResource(R.string.eliminar),
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}