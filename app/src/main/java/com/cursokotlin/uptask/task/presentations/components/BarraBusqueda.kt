package com.cursokotlin.uptask.task.presentations.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cursokotlin.uptask.R

@Composable
fun BarraBusqueda(
    texto: String,
    alCambiarTexto: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = texto,
        onValueChange = alCambiarTexto,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text(text = stringResource(R.string.tareas_buscar)) },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_buscar),
                contentDescription = stringResource(R.string.buscar),
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface
        )
    )
}