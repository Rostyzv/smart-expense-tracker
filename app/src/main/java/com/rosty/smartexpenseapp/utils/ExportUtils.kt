package com.rosty.smartexpenseapp.utils

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import androidx.core.content.FileProvider
import com.rosty.smartexpenseapp.model.Expense
import java.io.File
import java.io.FileOutputStream

object ExportUtils {


    fun shareExpensesCsv(context: Context, expenses: List<Expense>, period: String) {
        val fileName = "Informe_Gastos_${period}.csv"
        val csvHeader = "FECHA;TITULO;CATEGORIA;MONTO\n"
        val csvData = expenses.joinToString("\n") {
            "${it.date};${it.title};${it.category};${it.amount}€"
        }

        saveAndShare(context, fileName, csvHeader + csvData, "text/csv")
    }

    fun shareExpensesPdf(context: Context, expenses: List<Expense>, period: String) {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas
        val paint = Paint()

        paint.color = android.graphics.Color.rgb(33, 150, 243)
        canvas.drawRect(0f, 0f, 595f, 80f, paint)

        paint.color = android.graphics.Color.WHITE
        paint.textSize = 24f
        paint.isFakeBoldText = true
        canvas.drawText("Mi Informe de Gastos", 40f, 45f, paint)

        paint.textSize = 14f
        paint.isFakeBoldText = false
        canvas.drawText("Periodo: $period | Generado: ${java.time.LocalDate.now()}", 40f, 65f, paint)

        var yPosition = 130f

        paint.color = android.graphics.Color.DKGRAY
        paint.isFakeBoldText = true
        canvas.drawText("FECHA", 40f, yPosition, paint)
        canvas.drawText("CONCEPTO", 140f, yPosition, paint)
        canvas.drawText("CATEGORÍA", 340f, yPosition, paint)
        canvas.drawText("MONTO", 480f, yPosition, paint)

        yPosition += 10f
        canvas.drawLine(40f, yPosition, 550f, yPosition, paint)
        yPosition += 25f

        paint.isFakeBoldText = false
        expenses.forEachIndexed { index, expense ->
            if (yPosition < 800f) {
                if (index % 2 == 0) {
                    paint.color = android.graphics.Color.rgb(245, 245, 245)
                    canvas.drawRect(35f, yPosition - 18f, 560f, yPosition + 7f, paint)
                }

                paint.color = android.graphics.Color.BLACK
                canvas.drawText(expense.date, 40f, yPosition, paint)
                canvas.drawText(expense.title, 140f, yPosition, paint)

                paint.color = when(expense.category) {
                    "Comida" -> android.graphics.Color.rgb(255, 152, 0)
                    "Transporte" -> android.graphics.Color.rgb(76, 175, 80)
                    else -> android.graphics.Color.DKGRAY
                }
                canvas.drawText(expense.category, 340f, yPosition, paint)

                paint.color = android.graphics.Color.BLACK
                paint.isFakeBoldText = true
                canvas.drawText("${expense.amount}€", 480f, yPosition, paint)
                paint.isFakeBoldText = false

                yPosition += 30f
            }
        }

        yPosition += 20f
        paint.color = android.graphics.Color.rgb(33, 150, 243)
        paint.textSize = 18f
        paint.isFakeBoldText = true
        val total = expenses.sumOf { it.amount }
        canvas.drawText("TOTAL: ${String.format("%.2f", total)}€", 400f, yPosition, paint)

        pdfDocument.finishPage(page)

        val file = File(context.cacheDir, "Informe_Gastos.pdf")
        pdfDocument.writeTo(FileOutputStream(file))
        pdfDocument.close()
        shareFile(context, file, "application/pdf")
    }

    private fun shareFile(context: Context, file: File, type: String) {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            this.type = type
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Compartir Informe"))
    }

    private fun saveAndShare(context: Context, name: String, content: String, type: String) {
        val file = File(context.cacheDir, name)
        file.writeText(content)
        shareFile(context, file, type)
    }
}