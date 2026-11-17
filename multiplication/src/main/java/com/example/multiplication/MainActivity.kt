package com.example.multiplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val numberInput = findViewById<EditText>(R.id.selectedNumber)

        findViewById<Button>(R.id.allButton).setOnClickListener {
            startActivity(Intent(this, QuizActivity::class.java))
        }

        findViewById<Button>(R.id.selectedButton).setOnClickListener {
            val number = numberInput.text.toString().toIntOrNull()
            if (number == null || number !in 2..9) {
                numberInput.error = "Введите число от 2 до 9"
            } else {
                startActivity(Intent(this, QuizActivity::class.java).putExtra("selectedFactor", number))
            }
        }
    }
}
