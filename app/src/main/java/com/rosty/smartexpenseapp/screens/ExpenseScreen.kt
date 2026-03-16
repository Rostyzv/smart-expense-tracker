package com.rosty.smartexpenseapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rosty.smartexpenseapp.components.*
import com.rosty.smartexpenseapp.model.Expense
import com.rosty.smartexpenseapp.network.RetrofitClient
import kotlinx.coroutines.launch
import com.rosty.smartexpenseapp.components.SwipeableExpenseCard
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.rosty.smartexpenseapp.model.PeriodType
import com.rosty.smartexpenseapp.test.TestList

@OptIn(ExperimentalMaterial3Api::class)
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

    var showExportMenu by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    val context = androidx.compose.ui.platform.LocalContext.current // <--- ESTA ES LA CLAVE


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
        floatingActionButton = {
            Column(horizontalAlignment = androidx.compose.ui.Alignment.End) {
                SmallFloatingActionButton(
                    onClick = { showExportMenu = true },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(androidx.compose.material.icons.Icons.Default.Share, contentDescription = "Exportar")
                }
                AddExpenseButton { showDialog = true }
            }
        }
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
        if (showExportMenu) {
            ModalBottomSheet(
                onDismissRequest = { showExportMenu = false },
                sheetState = sheetState
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    Text("Exportar Informe", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(16.dp))

                    ListItem(
                        headlineContent = { Text("Documento PDF (Visual)") },
                        leadingContent = { Icon(androidx.compose.material.icons.Icons.Default.PictureAsPdf, null) },
                        modifier = Modifier.clickable {
                            com.rosty.smartexpenseapp.utils.ExportUtils.shareExpensesPdf(context, expensesMostrados, periodoSeleccionado.name)
                            showExportMenu = false
                        }
                    )
                    ListItem(
                        headlineContent = { Text("Archivo Excel/CSV (Datos)") },
                        leadingContent = { Icon(androidx.compose.material.icons.Icons.Default.TableChart, null) },
                        modifier = Modifier.clickable {
                            com.rosty.smartexpenseapp.utils.ExportUtils.shareExpensesCsv(context, expensesMostrados, periodoSeleccionado.name)
                            showExportMenu = false
                        }
                    )
                    Spacer(Modifier.height(32.dp))
                }
            }
        }
    }
}
