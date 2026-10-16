package com.cursokotlin.uptask.task.presentations.screens

import androidx.annotation.StringRes
import com.cursokotlin.uptask.R

enum class Prioridad(@StringRes val texto: Int) {
    BAJA(R.string.prioridad_baja),
    MEDIA(R.string.prioridad_media),
    ALTA(R.string.prioridad_alta)
}