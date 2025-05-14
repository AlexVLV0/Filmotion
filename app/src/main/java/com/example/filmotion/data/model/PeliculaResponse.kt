package com.example.filmotion.data.model

import com.example.filmotion.model.Pelicula

data class PeliculaResponse(
    val status: String,
    val pelicula: Pelicula?
)
