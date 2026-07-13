package com.ocordero.android_part.services

import com.ocordero.android_part.model.Cita
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface ApiService {

    // Obtener todas las citas
    @GET("citas")
    suspend fun obtenerCitas(): Response<List<Cita>>

    // Obtener una cita por ID
    @GET("citas/{id}")
    suspend fun obtenerCita(@Path("id") id: Int): Response<Cita>

    // Crear una nueva cita
    @POST("citas")
    suspend fun crearCita(@Body cita: Cita): Response<Cita>

    // Actualizar una cita existente
    @PUT("citas/{id}")
    suspend fun actualizarCita(@Path("id") id: Int, @Body cita: Cita): Response<Cita>

    // Eliminar una cita
    @DELETE("citas/{id}")
    suspend fun eliminarCita(@Path("id") id: Int): Response<Unit>
}