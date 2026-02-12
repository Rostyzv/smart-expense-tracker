package com.rosty.smartexpenseapp.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rosty.smartexpenseapp.model.Expense
import com.rosty.smartexpenseapp.model.PeriodType
import com.rosty.smartexpenseapp.utils.ExpenseUtils

// Tarjeta de balance: muestra el gasto total alineado a la izquierda según el filtro.
@Composable
fun TotalBalanceCard(
    expenses: List<Expense>,
    selectedPeriod: PeriodType
) {
    val displayAmount = ExpenseUtils.calculateDisplayAmount(expenses, selectedPeriod)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "GASTO ${selectedPeriod.label.uppercase()}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = ExpenseUtils.formatCurrency(displayAmount),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}