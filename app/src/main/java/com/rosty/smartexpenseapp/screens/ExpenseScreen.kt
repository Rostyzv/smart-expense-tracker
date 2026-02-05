package com.rosty.smartexpenseapp.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.rosty.smartexpenseapp.components.AddExpenseButton
import com.rosty.smartexpenseapp.components.AddExpenseDialog
import com.rosty.smartexpenseapp.components.ExpenseItem
import com.rosty.smartexpenseapp.model.Expense

import com.rosty.smartexpenseapp.network.RetrofitClient
import kotlinx.coroutines.launch

@Composable
fun ExpenseScreen() {
    // 1. Estado de la lista
    val expenses = remember { mutableStateListOf<Expense>() }
    var showDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // 2. Cargar datos al iniciar la App
    LaunchedEffect(Unit) {
        try {
            val listaDesdePython = RetrofitClient.instance.getExpenses()
            expenses.clear()
            expenses.addAll(listaDesdePython)
        } catch (e: Exception) {
            println("DEBUG: Error al cargar: ${e.message}")
        }
    }

    Scaffold(
        floatingActionButton = {
            AddExpenseButton(onClick = { showDialog = true })
        }
    ) { paddingValues ->

        if (showDialog) {
            AddExpenseDialog(
                onDismiss = { showDialog = false },
                onSave = { nombre, monto, categoria ->
                    val formatter = java.text.SimpleDateFormat("dd MMM", java.util.Locale.getDefault())
                    val fechaHoy = formatter.format(java.util.Date())

                    val nuevoGasto = Expense(
                        id = (0..10000).random(),
                        title = nombre,
                        amount = monto,
                        category = categoria,
                        date = fechaHoy
                    )

                    scope.launch {
                        try {
                            RetrofitClient.instance.addExpense(nuevoGasto)
                            expenses.add(nuevoGasto)
                            showDialog = false
                        } catch (e: Exception) {
                            println("DEBUG: Error al guardar: ${e.message}")
                        }
                    }
                }
            )
        }

        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(expenses) { expense ->
                ExpenseItem(expense = expense)
            }
        }
    }
}