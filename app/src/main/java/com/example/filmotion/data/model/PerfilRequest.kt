package com.example.filmotion.data.model

data class PerfilRequest(
    val email: String,
    val action: String = "getPerfil"
)