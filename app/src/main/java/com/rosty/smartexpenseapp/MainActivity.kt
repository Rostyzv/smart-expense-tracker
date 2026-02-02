package com.rosty.smartexpenseapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import com.rosty.smartexpenseapp.model.Expense
import com.rosty.smartexpenseapp.screens.ExpenseScreen
import com.rosty.smartexpenseapp.ui.theme.SmartExpenseAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SmartExpenseAppTheme {
                val expenses = remember {
                    mutableStateListOf(
                        Expense(1, "Netflix", 12.99, "Ocio", "01/05"),
                        Expense(2, "Cena Pizza", 25.50, "Comida", "02/05")
                    )
                }

                ExpenseScreen(
                    expenses = expenses,
                    onAddExpense = { nombre, monto ->
                        expenses.add(
                            Expense(
                                id = expenses.size + 1,
                                title = nombre,
                                amount = monto,
                                category = "General",
                                date = "Hoy"
                            )
                        )
                    }
                )
            }
        }
    }
}