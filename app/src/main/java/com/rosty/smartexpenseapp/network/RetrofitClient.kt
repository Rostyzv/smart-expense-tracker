package com.rosty.smartexpenseapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Cliente API: configura la conexión con el servidor para enviar y recibir datos.
object RetrofitClient {
    // Hay que poner la ip local despues de montar el servidor
    //private const val BASE_URL = "http://10.0.2.2:8000/"
    private const val BASE_URL = "http://192.168.0.24:8001/"

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }
}