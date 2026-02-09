package com.rosty.smartexpenseapp.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun getCategoryColor(category: String): Color {
    val scheme = MaterialTheme.colorScheme
    return when (category.lowercase().trim()) {
        "comida" -> scheme.primary
        "transporte" -> scheme.secondary
        "ocio" -> scheme.tertiary
        "salud" -> scheme.error
        "hogar" -> scheme.onBackground
        else -> scheme.outline
    }
}

@Composable
fun getCategoryIcon(category: String): ImageVector {
    return when (category.lowercase().trim()) {
        "comida" -> Icons.Default.Restaurant
        "transporte" -> Icons.Default.DirectionsCar
        "ocio" -> Icons.Default.ConfirmationNumber
        "salud" -> Icons.Default.LocalHospital
        "hogar" -> Icons.Default.Home
        else -> Icons.Default.Category
    }
}