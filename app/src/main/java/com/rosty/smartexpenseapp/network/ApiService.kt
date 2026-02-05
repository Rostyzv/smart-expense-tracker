package com.rosty.smartexpenseapp.network

import com.rosty.smartexpenseapp.model.Expense
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("expenses")
    suspend fun getExpenses(): List<Expense>

    @POST("expenses")
    suspend fun addExpense(@Body expense: Expense): Any
}