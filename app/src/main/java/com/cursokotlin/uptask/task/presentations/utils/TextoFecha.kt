package com.cursokotlin.uptask.task.presentations.utils

import androidx.annotation.StringRes
import com.cursokotlin.uptask.R

// Qué texto de strings.xml usar para mostrar cuándo vence algo, y con cuántos días
data class TextoFecha(
    @StringRes val recurso: Int,
    val dias: Int = 0
)

// Decide el texto según los días que faltan para la entrega
fun textoVencimiento(dias: Int): TextoFecha {
    return when {
        dias < 0 -> TextoFecha(R.string.vence_atrasada)
        dias == 0 -> TextoFecha(R.string.vence_hoy)
        dias == 1 -> TextoFecha(R.string.vence_manana)
        else -> TextoFecha(R.string.vence_en_dias, dias)
    }
}