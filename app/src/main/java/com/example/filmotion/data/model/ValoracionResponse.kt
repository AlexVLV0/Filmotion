package com.example.filmotion.data.model

data class ValoracionResponse(
    val status: String,
    val valoracion: Valoracion?
)

data class Valoracion(
    val id: Int,
    val id_usuario: Int,
    val id_pelicula: Int,
    val puntuacion: Int,
    val emocion: Int,
    val fecha: String
)
