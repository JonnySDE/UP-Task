package com.cursokotlin.uptask.task.presentations.utils

import kotlin.math.roundToInt

// Porcentaje = parte / total. Si el total es 0, el resultado es 0.
fun calcularPorcentaje(parte: Int, total: Int): Int {
    if (total == 0) return 0
    return (parte * 100f / total).roundToInt()
}