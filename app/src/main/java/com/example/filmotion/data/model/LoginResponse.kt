package com.example.filmotion.data.model

data class LoginResponse(
    val status: String,
    val message: String,
    val token: String?, // Nuevo campo
    val user_id: Int?,
    val email: String?
)
