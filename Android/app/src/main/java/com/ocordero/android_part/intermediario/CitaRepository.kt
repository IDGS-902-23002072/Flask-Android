package com.ocordero.android_part.intermediario

import com.ocordero.android_part.model.Cita
import com.ocordero.android_part.services.RetrofitClient

class CitaRepository {

    private val api = RetrofitClient.apiService

    // Obtener todas las citas
    suspend fun obtenerCitas(): Result<List<Cita>> {
        return try {
            val response = api.obtenerCitas()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error del servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Obtener una cita por ID
    suspend fun obtenerCita(id: Int): Result<Cita> {
        return try {
            val response = api.obtenerCita(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error del servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Crear una nueva cita
    suspend fun crearCita(cita: Cita): Result<Cita> {
        return try {
            val response = api.crearCita(cita)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al crear la cita: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Actualizar una cita existente
    suspend fun actualizarCita(id: Int, cita: Cita): Result<Cita> {
        return try {
            val response = api.actualizarCita(id, cita)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al actualizar la cita: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Eliminar una cita
    suspend fun eliminarCita(id: Int): Result<Unit> {
        return try {
            val response = api.eliminarCita(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al eliminar la cita: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}