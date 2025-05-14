package com.example.filmotion.data.api

import com.example.filmotion.data.model.LoginResponse
import com.example.filmotion.data.model.OlvidadaResponse
import com.example.filmotion.data.model.PeliculaResponse
import com.example.filmotion.data.model.PerfilRequest
import com.example.filmotion.data.model.PerfilResponse
import com.example.filmotion.data.model.ValoracionResponse
import com.example.filmotion.model.Pelicula
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("public/index.php")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("action") action: String = "login"
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("public/index.php")
    fun registerUser(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("action") action: String = "register"
    ): Call<LoginResponse>

    @POST("index.php")
    fun getPerfil(@Body request: PerfilRequest): Call<PerfilResponse>

    @GET("public/index.php")
    fun buscarPeliculas(
        @Query("action") action: String = "buscarPeliculas",
        @Query("query") query: String
    ): Call<List<Pelicula>>

    @FormUrlEncoded
    @POST("public/index.php")
    fun guardarValoracion(
        @Field("id_usuario") idUsuario: Int,
        @Field("id_pelicula") idPelicula: Int,
        @Field("puntuacion") puntuacion: Int,
        @Field("emocion") emocion: Int,
        @Field("action") action: String = "guardarValoracion"
    ): Call<Void>

    @FormUrlEncoded
    @POST("public/index.php")
    fun consultarValoracion(
        @Field("id_usuario") idUsuario: Int,
        @Field("id_pelicula") idPelicula: Int,
        @Field("action") action: String = "consultarValoracion"
    ): Call<ValoracionResponse>


    @FormUrlEncoded
    @POST("public/index.php")
    fun getPeliculasValoradas(
        @Field("id_usuario") idUsuario: Int,
        @Field("action") action: String = "getPeliculasValoradas"
    ): Call<List<Pelicula>>

    @FormUrlEncoded
    @POST("public/index.php")
    fun getOlvidada(
        @Field("id_usuario") idUsuario: Int,
        @Field("action") action: String = "getOlvidada"
    ): Call<OlvidadaResponse>

    @FormUrlEncoded
    @POST("public/index.php")
    fun getRecomendaciones(
        @Field("id_usuario") idUsuario: Int,
        @Field("emocion") emocion: String,
        @Field("action") action: String = "getRecomendacion"
    ): Call<List<Pelicula>>




}

