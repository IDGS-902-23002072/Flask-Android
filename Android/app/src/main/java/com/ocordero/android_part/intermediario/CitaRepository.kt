package com.ocordero.android_part.intermediario

import com.ocordero.android_part.model.Cita
import com.ocordero.android_part.services.RetrofitClient
import com.google.gson.JsonSyntaxException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class CitaRepository {

    private val api = RetrofitClient.apiService

    // Obtener todas las citas
    suspend fun obtenerCitas(): Result<List<Cita>> {
        return try {
            val response = api.obtenerCitas()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val mensaje = if (response.code() >= 500)
                    "Error interno del servidor (${response.code()})"
                else
                    "Error del servidor: ${response.code()}"
                Result.failure(Exception(mensaje))
            }
        } catch (e: Exception) {
            Result.failure(manejarError(e))
        }
    }

    // Obtener una cita por ID
    suspend fun obtenerCita(id: Int): Result<Cita> {
        return try {
            val response = api.obtenerCita(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val mensaje = if (response.code() >= 500)
                    "Error interno del servidor (${response.code()})"
                else
                    "Error del servidor: ${response.code()}"
                Result.failure(Exception(mensaje))
            }
        } catch (e: Exception) {
            Result.failure(manejarError(e))
        }
    }

    // Crear una nueva cita
    suspend fun crearCita(cita: Cita): Result<Cita> {
        return try {
            val response = api.crearCita(cita)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val mensaje = if (response.code() >= 500)
                    "Error interno del servidor (${response.code()})"
                else
                    "Error del servidor: ${response.code()}"
                Result.failure(Exception(mensaje))
            }
        } catch (e: Exception) {
            Result.failure(manejarError(e))
        }
    }

    // Actualizar una cita existente
    suspend fun actualizarCita(id: Int, cita: Cita): Result<Cita> {
        return try {
            val response = api.actualizarCita(id, cita)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val mensaje = if (response.code() >= 500)
                    "Error interno del servidor (${response.code()})"
                else
                    "Error del servidor: ${response.code()}"
                Result.failure(Exception(mensaje))
            }
        } catch (e: Exception) {
            Result.failure(manejarError(e))
        }
    }

    // Eliminar una cita
    suspend fun eliminarCita(id: Int): Result<Unit> {
        return try {
            val response = api.eliminarCita(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val mensaje = if (response.code() >= 500)
                    "Error interno del servidor (${response.code()})"
                else
                    "Error del servidor: ${response.code()}"
                Result.failure(Exception(mensaje))
            }
        } catch (e: Exception) {
            Result.failure(manejarError(e))
        }
    }

    private fun manejarError(e: Exception): Exception {
        return when (e) {
            is UnknownHostException ->
                Exception("Sin conexión a Internet. Verifica tu red.")
            is ConnectException ->
                Exception("No se pudo conectar con el servidor. ¿Está encendido Flask?")
            is SocketTimeoutException ->
                Exception("El servidor tardó demasiado en responder.")
            is JsonSyntaxException ->
                Exception("Error al procesar los datos del servidor.")
            is IOException ->
                Exception("Error de red. Verifica tu conexión.")
            else ->
                Exception("Ocurrió un error inesperado: ${e.message}")
        }
    }
}