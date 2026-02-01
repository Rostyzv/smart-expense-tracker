package com.rosty.smartexpenseapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rosty.smartexpenseapp.model.Expense
import com.rosty.smartexpenseapp.screens.ExpenseScreen
import com.rosty.smartexpenseapp.ui.theme.SmartExpenseAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SmartExpenseAppTheme {
                // Datos de prueba para ver algo en pantalla
                val dummyExpenses = listOf(
                    Expense(1, "Netflix", 12.99, "Ocio", "01/05"),
                    Expense(2, "Cena Pizza", 25.50, "Comida", "02/05"),
                    Expense(3, "Bus", 1.50, "Transporte", "03/05")
                )

                ExpenseScreen(expenses = dummyExpenses)
            }
        }
    }
}