package com.rosty.smartexpenseapp.components


import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rosty.smartexpenseapp.model.PeriodType

// Selector de tiempo: fila horizontal con flechas animadas y degradados de transparencia.
@Composable
fun PeriodSelector(
    selectedPeriod: PeriodType,
    onPeriodSelected: (PeriodType) -> Unit
) {
    val scrollState = rememberLazyListState()
    val showLeftArrow by remember { derivedStateOf { scrollState.canScrollBackward } }
    val showRightArrow by remember { derivedStateOf { scrollState.canScrollForward } }

    Box(modifier = Modifier.fillMaxWidth().height(56.dp)) {
        LazyRow(
            state = scrollState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(PeriodType.entries) { periodo ->
                FilterChip(
                    selected = selectedPeriod == periodo,
                    onClick = { onPeriodSelected(periodo) },
                    label = { Text(periodo.label) },
                    leadingIcon = if (selectedPeriod == periodo) {
                        { Icon(Icons.Default.Check, null, modifier = Modifier.size(18.dp)) }
                    } else null
                )
            }
        }

        ArrowGradient(Alignment.CenterStart, showLeftArrow, Icons.AutoMirrored.Filled.KeyboardArrowLeft)
        ArrowGradient(Alignment.CenterEnd, showRightArrow, Icons.AutoMirrored.Filled.KeyboardArrowRight)
    }
}

@Composable
private fun BoxScope.ArrowGradient(
    align: Alignment,
    visible: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(), exit = fadeOut(),
        modifier = Modifier.align(align)
    ) {
        val brush = if (align == Alignment.CenterStart)
            Brush.horizontalGradient(listOf(MaterialTheme.colorScheme.surface, Color.Transparent))
        else
            Brush.horizontalGradient(listOf(Color.Transparent, MaterialTheme.colorScheme.surface))

        Box(
            modifier = Modifier.fillMaxHeight().width(60.dp).background(brush),
            contentAlignment = align
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}