package com.ocordero.android_part.model

data class Cita(
    val id: Int? = null,
    val paciente: String,
    val doctor: String,
    val especialidad: String,
    val fecha: String,
    val hora: String,
    val motivo: String? = "",
    val estado: String? = "Pendiente"
)