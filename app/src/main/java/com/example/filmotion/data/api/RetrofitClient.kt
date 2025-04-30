package com.example.filmotion.data.api

import android.content.Context
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://0977-157-88-196-230.ngrok-free.app/filmotion-api/"

    // Necesitarás un contexto si accedes a SharedPreferences
    fun create(context: Context): ApiService {
        val sharedPrefs = context.getSharedPreferences("filmotion", Context.MODE_PRIVATE)
        val token = sharedPrefs.getString("token", "") ?: ""

        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(request)
        }.build()

        val gson = GsonBuilder().setLenient().create()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }
}

