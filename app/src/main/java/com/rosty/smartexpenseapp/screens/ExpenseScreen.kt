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
import com.rosty.smartexpenseapp.components.SwipeableExpenseCard
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.rosty.smartexpenseapp.model.PeriodType
import com.rosty.smartexpenseapp.test.TestList

// Pantalla principal: gestiona datos de la API, estados y organiza la interfaz completa.
@Composable
fun ExpenseScreen() {
    val expenses = remember { mutableStateListOf<Expense>() }
    var showDialog by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var expenseToDelete by remember { mutableStateOf<Expense?>(null) }
    var periodoSeleccionado by remember { mutableStateOf(PeriodType.MES) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val hoy = remember { LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")) }
    val listState = rememberLazyListState()


    val expensesMostrados = remember(expenses.toList(), periodoSeleccionado) {
        val hoy = java.time.LocalDate.now()
        val formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val anioActual = hoy.year

        expenses.filter { gasto ->
            val cumpleFiltro: Boolean = try {
                var fechaGasto = java.time.LocalDate.parse("${gasto.date}/$anioActual", formatter)
                if (fechaGasto.isAfter(hoy)) { fechaGasto = fechaGasto.minusYears(1) }

                when (periodoSeleccionado) {
                    PeriodType.DIA -> fechaGasto.isEqual(hoy)
                    PeriodType.SEMANA -> !fechaGasto.isBefore(hoy.minusWeeks(1))
                    PeriodType.MES -> !fechaGasto.isBefore(hoy.minusMonths(1))
                    PeriodType.TRIMESTRE -> !fechaGasto.isBefore(hoy.minusMonths(3))
                    PeriodType.ANYO -> fechaGasto.year == anioActual || fechaGasto.year == anioActual - 1
                    PeriodType.TODO -> true
                }
            } catch (e: Exception) {
                periodoSeleccionado == PeriodType.TODO
            }
            cumpleFiltro
        }.sortedByDescending { it.id }
    }


    LaunchedEffect(periodoSeleccionado) {
        if (expensesMostrados.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }

    val loadExpenses = {
        scope.launch {
            try {
                val lista = TestList.getTestExpenses()
                expenses.clear()
                expenses.addAll(lista)
            } catch (e: Exception) { println("Error: ${e.message}") }
        }
    }

    LaunchedEffect(Unit) { loadExpenses() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = { AddExpenseButton { showDialog = true } }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {

            TotalBalanceCard(expenses = expenses, selectedPeriod = periodoSeleccionado)

            PeriodSelector(periodoSeleccionado) { periodoSeleccionado = it }

            LazyColumn(
                state = listState, // VINCULACIÓN DEL SCROLL
                modifier = Modifier.fillMaxSize().weight(1f)
            ) {
                items(items = expensesMostrados, key = { it.id ?: it.hashCode() }) { expense ->
                    Box(modifier = Modifier.animateItem()) {
                        SwipeableExpenseCard(onDeleteRequest = {
                            expenseToDelete = expense
                            showDeleteConfirm = true
                        }) {
                            ExpenseItem(expense = expense)
                        }
                    }
                }
            }
        }

        // --- DIÁLOGOS ---

        if (showDialog) {
            AddExpenseDialog(
                onDismiss = { showDialog = false },
                onSave = { nombre, monto, categoria ->
                    scope.launch {
                        try {
                            val nuevo = Expense(0, nombre, monto, categoria, hoy)
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
            DeleteConfirmDialog(
                expenseTitle = expenseToDelete?.title,
                onDismiss = { showDeleteConfirm = false },
                onConfirm = {
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
                }
            )
        }
    }
}
