package com.rosty.smartexpenseapp.utils

import com.rosty.smartexpenseapp.model.Expense
import com.rosty.smartexpenseapp.model.PeriodType
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

// Utilidades: lógica para formatear moneda y filtrar sumas de gastos por periodos.
object ExpenseUtils {

    fun calculateDisplayAmount(expenses: List<Expense>, period: PeriodType): Double {
        val hoy = java.time.LocalDate.now()
        val formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val anioActual = hoy.year

        return expenses.filter { gasto ->
            try {
                var fechaGasto = java.time.LocalDate.parse("${gasto.date}/$anioActual", formatter)

                if (fechaGasto.isAfter(hoy)) {
                    fechaGasto = fechaGasto.minusYears(1)
                }

                when (period) {
                    PeriodType.DIA -> fechaGasto.isEqual(hoy)
                    PeriodType.SEMANA -> !fechaGasto.isBefore(hoy.minusWeeks(1))
                    PeriodType.MES -> !fechaGasto.isBefore(hoy.minusMonths(1))
                    PeriodType.TRIMESTRE -> !fechaGasto.isBefore(hoy.minusMonths(3))
                    PeriodType.ANYO -> fechaGasto.year == anioActual || fechaGasto.year == anioActual - 1
                    PeriodType.TODO -> true
                }
            } catch (e: Exception) {
                period == PeriodType.TODO
            }
        }.sumOf { it.amount }
    }

    fun formatCurrency(amount: Double): String {
        return "%.2f €".format(java.util.Locale.getDefault(), amount)
    }
}