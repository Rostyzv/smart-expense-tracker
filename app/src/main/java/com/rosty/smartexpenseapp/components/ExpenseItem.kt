package com.rosty.smartexpenseapp.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.BorderStroke
import com.rosty.smartexpenseapp.model.Expense

@Composable
fun ExpenseItem(expense: Expense) {
    // Extraemos la magia de CategoryUtils
    val categoryColor = getCategoryColor(expense.category)
    val categoryIcon = getCategoryIcon(expense.category)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        // Añadimos un borde sutil del color de la categoría para darle un toque pro
        border = BorderStroke(1.dp, categoryColor.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono de categoría a la izquierda
            Icon(
                imageVector = categoryIcon,
                contentDescription = null,
                tint = categoryColor,
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 12.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = expense.title,
                    style = MaterialTheme.typography.titleLarge
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = expense.category,
                        style = MaterialTheme.typography.bodyMedium,
                        color = categoryColor // <-- Ahora el texto fluye con la categoría
                    )
                    Text(text = " • ", color = Color.Gray)
                    Text(
                        text = expense.date,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            Text(
                text = "${String.format("%.2f", expense.amount)}€",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface // Un color más neutro para el monto
            )
        }
    }
}