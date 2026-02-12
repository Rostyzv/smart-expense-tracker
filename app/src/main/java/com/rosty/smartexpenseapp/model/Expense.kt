package com.rosty.smartexpenseapp.model

// Modelo de datos: estructura que define las propiedades de un gasto (ID, título, monto).
data class Expense(
    val id: Int = 0,
    val title: String,
    val amount: Double,
    val category: String = "General",
    val date: String = ""
)
