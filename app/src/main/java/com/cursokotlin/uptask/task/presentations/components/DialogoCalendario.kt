package com.cursokotlin.uptask.task.presentations.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.cursokotlin.uptask.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogoCalendario(
    fechaInicial: Long,
    alConfirmar: (Long) -> Unit,
    alCancelar: () -> Unit
) {
    // El calendario recuerda el dia
    val estadoCalendario = rememberDatePickerState(initialSelectedDateMillis = fechaInicial)

    DatePickerDialog(
        onDismissRequest = alCancelar,
        confirmButton = {
            TextButton(
                onClick = {
                    val fecha = estadoCalendario.selectedDateMillis
                    if (fecha != null) alConfirmar(fecha) else alCancelar()
                }
            ) {
                Text(text = stringResource(R.string.aceptar))
            }
        },
        dismissButton = {
            TextButton(onClick = alCancelar) {
                Text(text = stringResource(R.string.cancelar))
            }
        }
    ) {
        DatePicker(state = estadoCalendario)
    }
}