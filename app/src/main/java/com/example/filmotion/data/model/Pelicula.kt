package com.example.filmotion.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pelicula(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    @SerializedName("fecha_lanzamiento")
    val fechaLanzamiento: String,
    @SerializedName("imagen_url")
    val imagenUrl: String,
    @SerializedName("puntuacion")
    val puntuacion: Int?,     // Entre 1 y 5
    @SerializedName("emocion")
    val emocion: Int?         // 1 = feliz 😊, 0 = triste 😢
) : Parcelable
