package com.rosty.smartexpenseapp.network

import com.rosty.smartexpenseapp.model.Expense
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// Interfaz de red: define las rutas y métodos HTTP para interactuar con el servidor.
interface ApiService {
    @GET("expenses")
    suspend fun getExpenses(): List<Expense>

    @POST("expenses")
    suspend fun addExpense(@Body expense: Expense): Response<ResponseBody>

    @DELETE("expenses/{id}")
    suspend fun deleteExpense(@Path("id") id: Int): Response<ResponseBody>
}