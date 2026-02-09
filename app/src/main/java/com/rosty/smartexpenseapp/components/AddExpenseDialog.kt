package com.rosty.smartexpenseapp.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.BorderStroke

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
        shape = RoundedCornerShape(24.dp), // Bordes más suaves
        title = {
            Text("Nuevo Gasto", style = MaterialTheme.typography.headlineSmall)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Cambiamos a OutlinedTextField para un look más moderno
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Concepto") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Monto (€)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                // Selector de Categoría con estilo
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        // Usamos el color de la categoría seleccionada en el borde
                        border = BorderStroke(1.dp, getCategoryColor(categoriaSeleccionada))
                    ) {
                        Icon(
                            imageVector = getCategoryIcon(categoriaSeleccionada),
                            contentDescription = null,
                            tint = getCategoryColor(categoriaSeleccionada),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Categoría: $categoriaSeleccionada", color = MaterialTheme.colorScheme.onSurface)
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth(0.7f) // Un poco más estrecho para que no baile
                    ) {
                        categorias.forEach { cat ->
                            DropdownMenuItem(
                                leadingIcon = {
                                    Icon(
                                        imageVector = getCategoryIcon(cat),
                                        contentDescription = null,
                                        tint = getCategoryColor(cat),
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
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
            Button(
                shape = RoundedCornerShape(12.dp),
                onClick = {
                    val montoLimpio = amount.replace(",", ".").toDoubleOrNull() ?: 0.0
                    if (name.isNotBlank() && montoLimpio > 0) {
                        onSave(name, montoLimpio, categoriaSeleccionada)
                    }
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}