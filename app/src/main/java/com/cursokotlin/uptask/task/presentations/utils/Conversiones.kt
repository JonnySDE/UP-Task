package com.cursokotlin.uptask.task.presentations.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.cursokotlin.uptask.ui.theme.GrisMateria

fun String?.aColor(): Color {
    if (this == null) return GrisMateria
    return try {
        Color(android.graphics.Color.parseColor(this))
    } catch (e: IllegalArgumentException) {
        GrisMateria
    }
}

fun Color.aHex(): String {
    return "#" + Integer.toHexString(this.toArgb()).uppercase().padStart(8, '0')
}