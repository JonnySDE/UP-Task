package com.cursokotlin.uptask.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EsquemaClaro = lightColorScheme(
    primary = RojoPrincipal,
    onPrimary = Color.White,
    primaryContainer = RojoClaro,
    onPrimaryContainer = RojoPrincipal,
    background = FondoApp,
    onBackground = TextoOscuro,
    surface = Color.White,
    onSurface = TextoOscuro,
    onSurfaceVariant = TextoSecundario
)

@Composable
fun UpTaskTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaClaro,
        typography = Typography,
        content = content
    )
}