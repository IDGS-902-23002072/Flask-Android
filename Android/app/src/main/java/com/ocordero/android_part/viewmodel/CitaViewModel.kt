package com.ocordero.android_part.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ocordero.android_part.intermediario.CitaRepository
import com.ocordero.android_part.model.Cita
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CitaViewModel : ViewModel() {

    private val repository = CitaRepository()

    // Lista de citas
    private val _citas = MutableStateFlow<List<Cita>>(emptyList())
    val citas: StateFlow<List<Cita>> = _citas.asStateFlow()

    // Indicador de carga
    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    // Mensaje de error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Mensaje de éxito (para confirmar alta/edición/eliminación)
    private val _mensajeExito = MutableStateFlow<String?>(null)
    val mensajeExito: StateFlow<String?> = _mensajeExito.asStateFlow()

    init {
        obtenerCitas()
    }

    fun obtenerCitas() {
        viewModelScope.launch {
            _cargando.value = true
            _error.value = null

            val resultado = repository.obtenerCitas()

            resultado
                .onSuccess { lista ->
                    _citas.value = lista
                }
                .onFailure { excepcion ->
                    _error.value = "No se pudieron cargar las citas: ${excepcion.message}"
                }
            delay(600)
            _cargando.value = false
        }
    }

    fun crearCita(cita: Cita, onExito: () -> Unit = {}) {
        viewModelScope.launch {
            _cargando.value = true
            _error.value = null

            val resultado = repository.crearCita(cita)

            resultado
                .onSuccess {
                    _mensajeExito.value = "Cita registrada correctamente"
                    obtenerCitas()
                    onExito()
                }
                .onFailure { excepcion ->
                    _error.value = "No se pudo registrar la cita: ${excepcion.message}"
                }
            delay(600)
            _cargando.value = false
        }
    }

    fun actualizarCita(id: Int, cita: Cita, onExito: () -> Unit = {}) {
        viewModelScope.launch {
            _cargando.value = true
            _error.value = null

            val resultado = repository.actualizarCita(id, cita)

            resultado
                .onSuccess {
                    _mensajeExito.value = "Cita actualizada correctamente"
                    obtenerCitas()
                    onExito()
                }
                .onFailure { excepcion ->
                    _error.value = "No se pudo actualizar la cita: ${excepcion.message}"
                }
            delay(600)
            _cargando.value = false
        }
    }

    fun eliminarCita(id: Int) {
        viewModelScope.launch {
            _cargando.value = true
            _error.value = null

            val resultado = repository.eliminarCita(id)

            resultado
                .onSuccess {
                    _mensajeExito.value = "Cita eliminada correctamente"
                    obtenerCitas()
                }
                .onFailure { excepcion ->
                    _error.value = "No se pudo eliminar la cita: ${excepcion.message}"
                }
            delay(600)
            _cargando.value = false
        }
    }

    fun limpiarError() {
        _error.value = null
    }

    fun limpiarMensajeExito() {
        _mensajeExito.value = null
    }
}