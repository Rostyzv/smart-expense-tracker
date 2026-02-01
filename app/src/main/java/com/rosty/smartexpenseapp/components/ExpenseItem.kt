package com.rosty.smartexpenseapp.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rosty.smartexpenseapp.model.Expense

@Composable
fun ExpenseItem(expense: Expense) {
    val priceColor = if (expense.amount > 20.0) Color(0xFFB71C1C) else Color.Red
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = expense.title, style = MaterialTheme.typography.titleMedium)
                Text(text = expense.category, style = MaterialTheme.typography.bodySmall)
            }
            Text(
                text = "-${expense.amount}€",
                color = priceColor,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}