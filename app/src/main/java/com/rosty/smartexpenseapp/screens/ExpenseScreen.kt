package com.rosty.smartexpenseapp.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rosty.smartexpenseapp.components.AddExpenseButton
import com.rosty.smartexpenseapp.components.ExpenseItem
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.rosty.smartexpenseapp.model.Expense

@Composable
fun ExpenseScreen(expenses: List<Expense>) {
    Scaffold(
        floatingActionButton = {
            // Aquí llamamos a nuestro nuevo componente
            AddExpenseButton(onClick = {
                // Por ahora no hace nada, pronto abriremos un formulario
                println("Botón pulsado")
            })
        }
    ) { paddingValues ->
        // El contenido de la pantalla va aquí dentro
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(expenses) { expense ->
                ExpenseItem(expense = expense)
            }
        }
    }
}