package com.cursokotlin.uptask.task.presentations.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

const val MILISEGUNDOS_POR_DIA = 86_400_000L

// El calendario de Material trabaja con la medianoche en UTC.
// Esta función devuelve el día de HOY en ese mismo formato, para poder compararlos.
fun hoyEnMillisUtc(): Long {
    val local = Calendar.getInstance()
    val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    utc.clear()
    utc.set(local.get(Calendar.YEAR), local.get(Calendar.MONTH), local.get(Calendar.DAY_OF_MONTH))
    return utc.timeInMillis
}

// Convierte los milisegundos en texto, por ejemplo "13 de octubre, 2026"
fun formatearFecha(millis: Long): String {
    val formato = SimpleDateFormat("d 'de' MMMM, yyyy", Locale.forLanguageTag("es-MX"))
    formato.timeZone = TimeZone.getTimeZone("UTC")
    return formato.format(Date(millis))
}

// Fecha de hoy con la zona horaria del teléfono, por ejemplo "Martes 20 de octubre"
fun fechaDeHoyTexto(): String {
    val formato = SimpleDateFormat("EEEE d 'de' MMMM", Locale.forLanguageTag("es-MX"))
    return formato.format(Date()).replaceFirstChar { letra -> letra.uppercase() }
}