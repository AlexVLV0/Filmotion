package com.example.filmotion.data.model

import com.example.filmotion.model.Pelicula

data class OlvidadaResponse(
    val status: String,
    val pelicula: Pelicula?
)
