package com.example.filmotion.data.api

import com.example.filmotion.data.model.LoginResponse
import com.example.filmotion.data.model.PerfilRequest
import com.example.filmotion.data.model.PerfilResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Field
import retrofit2.http.POST

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

}

