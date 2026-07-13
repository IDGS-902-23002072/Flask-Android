package com.ocordero.android_part.view.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ocordero.android_part.model.Cita
import com.ocordero.android_part.view.components.ConfirmDialog
import com.ocordero.android_part.viewmodel.CitaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaCitasScreen(
    viewModel: CitaViewModel = viewModel(),
    onAgregarClick: () -> Unit,
    onEditarClick: (Int) -> Unit
) {
    val citas by viewModel.citas.collectAsState()
    val cargando by viewModel.cargando.collectAsState()
    val error by viewModel.error.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Citas Médicas") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAgregarClick) {
                Icon(Icons.Default.Add, contentDescription = "Agregar cita")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                cargando -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                error != null -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = error ?: "Ocurrió un error",
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = { viewModel.obtenerCitas() }) {
                            Text("Reintentar")
                        }
                    }
                }

                citas.isEmpty() -> {
                    Text(
                        text = "No hay citas registradas",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(citas) { cita ->
                            CitaItem(
                                cita = cita,
                                onEditar = { onEditarClick(cita.id ?: 0) },
                                onEliminar = { viewModel.eliminarCita(cita.id ?: 0) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CitaItem(
    cita: Cita,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    var mostrarDialogo by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = cita.paciente, style = MaterialTheme.typography.titleMedium)
                Text(text = "Dr(a). ${cita.doctor} · ${cita.especialidad}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "${cita.fecha} - ${cita.hora}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Estado: ${cita.estado ?: "Pendiente"}", style = MaterialTheme.typography.bodySmall)
            }

            Row {
                IconButton(onClick = onEditar) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = { mostrarDialogo = true }) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }
        }
    }

    if (mostrarDialogo) {
        ConfirmDialog(
            titulo = "Eliminar cita",
            mensaje = "¿Seguro que deseas eliminar la cita de ${cita.paciente}?",
            onConfirmar = {
                mostrarDialogo = false
                onEliminar()
            },
            onCancelar = { mostrarDialogo = false }
        )
    }
}
