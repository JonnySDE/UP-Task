package com.cursokotlin.uptask.task.presentations.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun UpTaskPage() {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { espacioSeguro ->
        InicioPage(
            alAgregarTarea = {},
            modifier = Modifier.padding(espacioSeguro)
        )
    }
}