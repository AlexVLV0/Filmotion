package com.example.filmotion.model

data class Pelicula(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val fechaLanzamiento: String,
    val imagenUrl: String,
    val duracion: Int,
    val genero: String
)
