package com.ocordero.android_part.view.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ocordero.android_part.model.Cita
import com.ocordero.android_part.view.components.mostrarDatePicker
import com.ocordero.android_part.view.components.mostrarTimePicker
import com.ocordero.android_part.viewmodel.CitaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarCitaScreen(
    citaId: Int,
    viewModel: CitaViewModel = viewModel(),
    onBack: () -> Unit
) {
    val citas by viewModel.citas.collectAsState()
    val cargando by viewModel.cargando.collectAsState()

    var cargaInicial by remember { mutableStateOf(true) }

    var paciente by remember { mutableStateOf("") }
    var doctor by remember { mutableStateOf("") }
    var especialidad by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var motivo by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("Pendiente") }

    var errorPaciente by remember { mutableStateOf<String?>(null) }
    var errorDoctor by remember { mutableStateOf<String?>(null) }
    var errorEspecialidad by remember { mutableStateOf<String?>(null) }
    var errorFecha by remember { mutableStateOf<String?>(null) }
    var errorHora by remember { mutableStateOf<String?>(null) }

    // Se ejecuta una sola vez cuando se abre la pantalla (o si cambia la lista de citas)
    LaunchedEffect(citas) {
        if (cargaInicial) {
            val cita = citas.find { it.id == citaId }
            if (cita != null) {
                paciente = cita.paciente
                doctor = cita.doctor
                especialidad = cita.especialidad
                fecha = cita.fecha
                hora = cita.hora
                motivo = cita.motivo ?: ""
                estado = cita.estado ?: "Pendiente"
                cargaInicial = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Cita") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (cargaInicial) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    OutlinedTextField(
                        value = paciente,
                        onValueChange = { paciente = it; errorPaciente = null },
                        label = { Text("Nombre del paciente") },
                        isError = errorPaciente != null,
                        supportingText = { errorPaciente?.let { Text(it) } },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = doctor,
                        onValueChange = { doctor = it; errorDoctor = null },
                        label = { Text("Nombre del doctor") },
                        isError = errorDoctor != null,
                        supportingText = { errorDoctor?.let { Text(it) } },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = especialidad,
                        onValueChange = { especialidad = it; errorEspecialidad = null },
                        label = { Text("Especialidad") },
                        isError = errorEspecialidad != null,
                        supportingText = { errorEspecialidad?.let { Text(it) } },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val context = LocalContext.current

                    OutlinedTextField(
                        value = fecha,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Fecha de la cita") },
                        isError = errorFecha != null,
                        supportingText = { errorFecha?.let { Text(it) } },
                        trailingIcon = {
                            IconButton(onClick = {
                                mostrarDatePicker(context) { fechaSeleccionada ->
                                    fecha = fechaSeleccionada
                                    errorFecha = null
                                }
                            }) {
                                Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                mostrarDatePicker(context) { fechaSeleccionada ->
                                    fecha = fechaSeleccionada
                                    errorFecha = null
                                }
                            }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = hora,
                        onValueChange = {},
                        readOnly = true,
                        enabled = fecha.isNotEmpty(),
                        label = { Text("Hora de la cita") },
                        isError = errorHora != null,
                        supportingText = { errorHora?.let { Text(it) } },
                        trailingIcon = {
                            IconButton(onClick = {
                                mostrarTimePicker(context, fecha) { horaSeleccionada, error ->
                                    if (error != null) {
                                        errorHora = error
                                    } else {
                                        hora = horaSeleccionada
                                        errorHora = null
                                    }
                                }
                            }) {
                                Icon(Icons.Default.AccessTime, contentDescription = "Seleccionar hora")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                mostrarTimePicker(context, fecha) { horaSeleccionada, error ->
                                    if (error != null) {
                                        errorHora = error
                                    } else {
                                        hora = horaSeleccionada
                                        errorHora = null
                                    }
                                }
                            }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = motivo,
                        onValueChange = { motivo = it },
                        label = { Text("Motivo (opcional)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = estado,
                        onValueChange = { estado = it },
                        label = { Text("Estado (Pendiente, Confirmada, Cancelada...)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    if (cargando) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    } else {
                        Button(
                            onClick = {
                                var esValido = true

                                if (paciente.trim().length < 3) {
                                    errorPaciente = "Debe tener al menos 3 caracteres"
                                    esValido = false
                                }
                                if (doctor.trim().length < 3) {
                                    errorDoctor = "Debe tener al menos 3 caracteres"
                                    esValido = false
                                }
                                if (especialidad.trim().isEmpty()) {
                                    errorEspecialidad = "Este campo es obligatorio"
                                    esValido = false
                                }

                                val regexFecha = Regex("""\d{4}-\d{2}-\d{2}""")
                                if (!regexFecha.matches(fecha.trim())) {
                                    errorFecha = "Formato inválido, usa YYYY-MM-DD"
                                    esValido = false
                                }

                                val regexHora = Regex("""\d{2}:\d{2}""")
                                if (!regexHora.matches(hora.trim())) {
                                    errorHora = "Formato inválido, usa HH:MM"
                                    esValido = false
                                }

                                if (esValido) {
                                    val citaActualizada = Cita(
                                        id = citaId,
                                        paciente = paciente.trim(),
                                        doctor = doctor.trim(),
                                        especialidad = especialidad.trim(),
                                        fecha = fecha.trim(),
                                        hora = hora.trim(),
                                        motivo = motivo.trim(),
                                        estado = estado.trim()
                                    )
                                    viewModel.actualizarCita(citaId, citaActualizada) {
                                        onBack()
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Actualizar cita")
                        }
                    }
                }
            }
        }
    }
}