package com.cursokotlin.uptask.task.presentations.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cursokotlin.uptask.R

@Composable
fun BotonAgregar(
    descripcion: String,
    alPresionar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(R.drawable.ic_agregar),
        contentDescription = descripcion,
        modifier = modifier
            .size(56.dp)
            .clickable(onClick = alPresionar)
    )
}