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

@Composable
fun ExpenseScreen(
    expenses: List<Expense>,
    onAddExpense: (String, Double) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            AddExpenseButton(onClick = { showDialog = true })
        }
    ) { paddingValues ->

        if (showDialog) {
            AddExpenseDialog(
                onDismiss = { showDialog = false },
                onSave = { nuevoNombre, nuevoMonto ->
                    onAddExpense(nuevoNombre, nuevoMonto)
                    showDialog = false
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