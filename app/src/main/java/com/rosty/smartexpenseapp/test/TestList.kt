package com.rosty.smartexpenseapp.test

import com.rosty.smartexpenseapp.model.Expense

object TestList {
    fun getTestExpenses(): List<Expense> {
        return listOf(
            // --- FILTRO: DÍA (Hoy 16/03) ---
            Expense(1, "Desayuno", 3.50, "Comida", "16/03"),

            // --- FILTRO: SEMANA (Últimos 7 días) ---
            Expense(2, "Compra Mercadona", 52.30, "Hogar", "14/03"),
            Expense(3, "Gasolina", 45.00, "Transporte", "11/03"),

            // --- FILTRO: MES (Últimos 30 días) ---
            Expense(4, "Cena Sushi", 28.00, "Ocio", "01/03"),
            Expense(5, "Suscripción Netflix", 12.99, "Ocio", "25/02"),

            // --- FILTRO: TRIMESTRE (Últimos 90 días) ---
            Expense(6, "Seguro Hogar", 85.00, "Hogar", "15/01"),
            Expense(7, "Ropa Invierno", 60.00, "Ocio", "10/01"),

            // --- FILTRO: AÑO (Año actual o anterior) ---
            Expense(8, "Cena Navidad", 40.00, "Comida", "24/12"), // Debería salir en AÑO y TODO
            Expense(9, "Gym Noviembre", 35.00, "Salud", "10/11")  // Debería salir en AÑO y TODO
        )
    }
}