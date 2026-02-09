package com.rosty.smartexpenseapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.rosty.smartexpenseapp.components.*
import com.rosty.smartexpenseapp.model.Expense
import com.rosty.smartexpenseapp.network.RetrofitClient
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.rosty.smartexpenseapp.components.SwipeableExpenseCard
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material.icons.filled.DeleteForever

import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

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

        // ORGANIZACIÓN VERTICAL
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Aplicamos el padding aquí una sola vez
        ) {
            // 1. LA TARJETA DEL TOTAL (Siempre arriba)
            TotalBalanceCard(expenses = expenses)

            // 2. LA LISTA (O EL ESTADO VACÍO)
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                if (expenses.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillParentMaxSize()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ReceiptLong,
                                contentDescription = null,
                                modifier = Modifier.size(120.dp),
                                tint = Color.Gray.copy(alpha = 0.3f)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("No hay gastos todavía", color = Color.Gray, style = MaterialTheme.typography.headlineSmall)
                            Text("Toca el botón '+' para añadir uno", color = Color.Gray.copy(alpha = 0.6f))
                        }
                    }
                } else {
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

        // --- DIÁLOGOS (Fuera de la Column para que se superpongan) ---
        if (showDialog) {
            AddExpenseDialog(
                onDismiss = { showDialog = false },
                onSave = { nombre, monto, categoria ->
                    scope.launch {
                        try {
                            val fechaReal = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM"))
                            val nuevo = Expense(0, nombre, monto, categoria, fechaReal)
                            RetrofitClient.instance.addExpense(nuevo)
                            loadExpenses()
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
                // Forma más moderna y redondeada
                shape = RoundedCornerShape(28.dp),
                containerColor = MaterialTheme.colorScheme.surface,
                icon = {
                    // Icono de advertencia destacado
                    Icon(
                        imageVector = Icons.Default.DeleteForever,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(40.dp)
                    )
                },
                title = {
                    Text(
                        text = "¿Eliminar gasto?",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Esta acción no se puede deshacer. El gasto de \"${expenseToDelete?.title}\" desaparecerá de tu historial.",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            expenseToDelete?.id?.let { id ->
                                scope.launch {
                                    try {
                                        RetrofitClient.instance.deleteExpense(id)
                                        expenses.remove(expenseToDelete)
                                        snackbarHostState.showSnackbar("Gasto eliminado")
                                    } catch (e: Exception) {
                                        snackbarHostState.showSnackbar("Error al eliminar")
                                    }
                                }
                            }
                            showDeleteConfirm = false
                        },
                        // Color de error del tema
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Eliminar")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDeleteConfirm = false }
                    ) {
                        Text("Cancelar", color = MaterialTheme.colorScheme.primary)
                    }
                }
            )
        }
    }
}
