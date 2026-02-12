package com.rosty.smartexpenseapp.test

import com.rosty.smartexpenseapp.model.Expense

object TestList {
    fun getTestExpenses(): List<Expense> {
        return listOf(
            Expense(1, "Café Mañanero", 2.50, "Comida", "12/02"),   // Hoy
            Expense(2, "Súper Semanal", 45.20, "Hogar", "10/02"),  // Esta Semana
            Expense(3, "Gasolina", 60.00, "Transporte", "05/02"),  // Esta Semana
            Expense(4, "Alquiler", 700.00, "Hogar", "01/02"),      // Este Mes
            Expense(5, "Cena Amigos", 35.00, "Ocio", "25/01"),     // Este Mes
            Expense(6, "Seguro Coche", 120.00, "Transporte", "15/12"), // Trimestre
            Expense(7, "Regalo Navidad", 50.00, "Ocio", "20/12"),  // Trimestre
            Expense(8, "Suscripción Gym", 30.00, "Salud", "10/11") // Fuera de Trimestre
        )
    }
}