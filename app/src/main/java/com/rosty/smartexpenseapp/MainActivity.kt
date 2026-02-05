package com.rosty.smartexpenseapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rosty.smartexpenseapp.screens.ExpenseScreen
import com.rosty.smartexpenseapp.ui.theme.SmartExpenseAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SmartExpenseAppTheme {
                ExpenseScreen()
            }
        }
    }
}