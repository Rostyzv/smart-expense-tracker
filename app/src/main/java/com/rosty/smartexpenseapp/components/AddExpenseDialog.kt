package com.rosty.smartexpenseapp.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun AddExpenseDialog(
    onDismiss: () -> Unit,
    onSave: (String, Double, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    val categorias = listOf("Comida", "Ocio", "Transporte", "Salud", "Hogar")
    var categoriaSeleccionada by remember { mutableStateOf(categorias[0]) }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo Gasto") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Concepto (ej. Cena)") },
                    modifier = Modifier.fillMaxWidth()
                )

                TextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Monto (€)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )


                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Categoría: $categoriaSeleccionada")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        categorias.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = {
                                    categoriaSeleccionada = cat
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val montoLimpio = amount.replace(",", ".").toDoubleOrNull() ?: 0.0

                if (name.isNotBlank() && montoLimpio > 0) {
                    onSave(name, montoLimpio, categoriaSeleccionada)
                }
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}