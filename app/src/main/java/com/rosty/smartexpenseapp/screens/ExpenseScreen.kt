package com.rosty.smartexpenseapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.rosty.smartexpenseapp.components.*
import com.rosty.smartexpenseapp.model.Expense
import com.rosty.smartexpenseapp.network.RetrofitClient
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Color

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning

import com.rosty.smartexpenseapp.components.SwipeableExpenseCard

@Composable
fun ExpenseScreen() {
    val expenses = remember { mutableStateListOf<Expense>() }
    var showDialog by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var expenseToDelete by remember { mutableStateOf<Expense?>(null) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val loadExpenses = {
        scope.launch {
            try {
                val lista = RetrofitClient.instance.getExpenses()
                expenses.clear()
                expenses.addAll(lista)
            } catch (e: Exception) {
                println("Error: ${e.message}")
            }
        }
    }

    LaunchedEffect(Unit) { loadExpenses() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = { AddExpenseButton { showDialog = true } }
    ) { paddingValues ->

        if (showDialog) {
            AddExpenseDialog(
                onDismiss = { showDialog = false },
                onSave = { nombre, monto, categoria ->
                    scope.launch {
                        try {
                            val nuevo = Expense(0, nombre, monto, categoria, "Hoy")
                            RetrofitClient.instance.addExpense(nuevo)
                            loadExpenses() // Recarga limpia
                            showDialog = false
                        } catch (e: Exception) {
                            snackbarHostState.showSnackbar("Error al guardar")
                        }
                    }
                }
            )
        }

        if (showDeleteConfirm) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirm = false },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color(0xFFD32F2F) // Rojo de advertencia
                    )
                },
                title = {
                    Text(text = "Confirmar eliminación")
                },
                text = {
                    Text("¿Estás seguro de que quieres borrar este gasto? Esta acción es permanente y no se podrá deshacer.")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            expenseToDelete?.id?.let { id ->
                                scope.launch {
                                    try {
                                        RetrofitClient.instance.deleteExpense(id)
                                        expenses.remove(expenseToDelete)
                                        snackbarHostState.showSnackbar("Gasto eliminado correctamente")
                                    } catch (e: Exception) {
                                        snackbarHostState.showSnackbar("Error: No se pudo eliminar")
                                    }
                                }
                            }
                            showDeleteConfirm = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)) // Botón rojo
                    ) {
                        Text("Eliminar", color = Color.White)
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = { showDeleteConfirm = false }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }

        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(
                items = expenses,
                key = { it.id ?: it.hashCode() }
            ) { expense ->
                Box(modifier = Modifier.animateItem()) {
                    SwipeableExpenseCard(
                        onDeleteRequest = {
                            expenseToDelete = expense
                            showDeleteConfirm = true
                        }
                    ) {
                        ExpenseItem(expense = expense)
                    }
                }
            }
        }
    }
}