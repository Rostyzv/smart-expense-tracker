package com.rosty.smartexpenseapp.model

data class Expense(
    val id: Int = 0,
    val title: String,
    val amount: Double,
    val category: String = "General",
    val date: String = ""
)
