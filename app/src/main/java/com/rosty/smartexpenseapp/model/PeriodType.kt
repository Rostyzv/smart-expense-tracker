package com.rosty.smartexpenseapp.model

// Tipos de periodo: define las opciones de filtrado (Día, Semana, Mes, Todo).
enum class PeriodType(val label: String) {
    DIA("Día"),
    SEMANA("Semana"),
    MES("Mes"),
    TRIMESTRE("Trimestre"),
    ANYO("Año"),
    TODO("Todo")
}