package com.example.quadratic

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlin.math.sqrt

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputA = findViewById<EditText>(R.id.inputA)
        val inputB = findViewById<EditText>(R.id.inputB)
        val inputC = findViewById<EditText>(R.id.inputC)
        val result = findViewById<TextView>(R.id.resultText)

        findViewById<Button>(R.id.solveButton).setOnClickListener {
            val a = inputA.text.toString().replace(',', '.').toDoubleOrNull()
            val b = inputB.text.toString().replace(',', '.').toDoubleOrNull()
            val c = inputC.text.toString().replace(',', '.').toDoubleOrNull()

            result.text = when {
                a == null || b == null || c == null -> "Введите три числа"
                a == 0.0 && b == 0.0 -> if (c == 0.0) "Бесконечно много решений" else "Нет решения"
                a == 0.0 -> "x = ${-c / b}"
                else -> {
                    val d = b * b - 4 * a * c
                    when {
                        d < 0 -> "Нет решения"
                        d == 0.0 -> "x = ${-b / (2 * a)}"
                        else -> {
                            val x1 = (-b + sqrt(d)) / (2 * a)
                            val x2 = (-b - sqrt(d)) / (2 * a)
                            "x₁ = $x1\nx₂ = $x2"
                        }
                    }
                }
            }
        }
    }
}
