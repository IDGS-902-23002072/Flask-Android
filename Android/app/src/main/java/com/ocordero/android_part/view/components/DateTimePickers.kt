package com.ocordero.android_part.view.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import java.util.Calendar

fun mostrarDatePicker(
    context: Context,
    onFechaSeleccionada: (String) -> Unit
) {
    val calendar = Calendar.getInstance()
    val anio = calendar.get(Calendar.YEAR)
    val mes = calendar.get(Calendar.MONTH)
    val dia = calendar.get(Calendar.DAY_OF_MONTH)

    val datePicker = DatePickerDialog(
        context,
        { _, anioSeleccionado, mesSeleccionado, diaSeleccionado ->
            val mesFormateado = String.format("%02d", mesSeleccionado + 1)
            val diaFormateado = String.format("%02d", diaSeleccionado)
            onFechaSeleccionada("$anioSeleccionado-$mesFormateado-$diaFormateado")
        },
        anio, mes, dia
    )

    // No permitir seleccionar fechas anteriores a hoy
    datePicker.datePicker.minDate = System.currentTimeMillis()
    datePicker.show()
}

fun mostrarTimePicker(
    context: Context,
    fechaSeleccionada: String,
    onHoraSeleccionada: (String, String?) -> Unit
) {
    val calendar = Calendar.getInstance()
    val horaActual = calendar.get(Calendar.HOUR_OF_DAY)
    val minutoActual = calendar.get(Calendar.MINUTE)

    val hoy = String.format(
        "%04d-%02d-%02d",
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH) + 1,
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val timePicker = TimePickerDialog(
        context,
        { _, horaSeleccionada, minutoSeleccionado ->
            val horaFormateada = String.format("%02d", horaSeleccionada)
            val minutoFormateado = String.format("%02d", minutoSeleccionado)
            val horaTexto = "$horaFormateada:$minutoFormateado"

            // Si la fecha elegida es HOY, validamos que la hora no haya pasado
            if (fechaSeleccionada == hoy) {
                if (horaSeleccionada < horaActual ||
                    (horaSeleccionada == horaActual && minutoSeleccionado < minutoActual)
                ) {
                    onHoraSeleccionada("", "No puedes elegir una hora que ya pasó")
                    return@TimePickerDialog
                }
            }

            onHoraSeleccionada(horaTexto, null)
        },
        horaActual, minutoActual, true
    )

    timePicker.show()
}