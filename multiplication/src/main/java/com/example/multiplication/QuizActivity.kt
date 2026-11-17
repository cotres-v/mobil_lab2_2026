package com.example.multiplication

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class QuizActivity : Activity() {
    private lateinit var engine: QuizEngine
    private lateinit var progressText: TextView
    private lateinit var questionText: TextView
    private lateinit var answerInput: EditText
    private lateinit var checkButton: Button
    private lateinit var feedbackText: TextView
    private lateinit var nextButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        progressText = findViewById(R.id.progressText)
        questionText = findViewById(R.id.questionText)
        answerInput = findViewById(R.id.answerInput)
        checkButton = findViewById(R.id.checkButton)
        feedbackText = findViewById(R.id.feedbackText)
        nextButton = findViewById(R.id.nextButton)

        val selected = intent.getIntExtra("selectedFactor", 0).takeIf { it != 0 }
        val snapshot = savedInstanceState?.let {
            QuizSnapshot(
                it.getInt("first"), it.getInt("second"), it.getInt("answered"),
                it.getInt("correct"), it.getBoolean("answeredCurrent"),
                it.getBoolean("lastAnswerCorrect")
            )
        }
        engine = QuizEngine(selected, saved = snapshot)
        showQuestion()

        checkButton.setOnClickListener {
            when (engine.submitAnswer(answerInput.text.toString())) {
                AnswerResult.INVALID -> answerInput.error = "Введите целое число"
                AnswerResult.CORRECT, AnswerResult.WRONG -> showQuestion()
                else -> Unit
            }
        }

        nextButton.setOnClickListener {
            if (engine.nextQuestion()) {
                answerInput.text.clear()
                showQuestion()
            }
        }
    }

    private fun showQuestion() {
        progressText.text = "Пример ${minOf(engine.answered + 1, 20)} из 20. Правильно: ${engine.correct}"
        questionText.text = "${engine.first} × ${engine.second} = ?"
        feedbackText.text = when {
            engine.isFinished -> "${if (engine.lastAnswerCorrect) "Правильный ответ" else "Неверный ответ"}\nРезультат: ${engine.percent}% (${engine.correct} из 20)"
            engine.answeredCurrent -> if (engine.lastAnswerCorrect) "Правильный ответ" else "Неверный ответ"
            else -> ""
        }
        answerInput.isEnabled = !engine.answeredCurrent && !engine.isFinished
        checkButton.isEnabled = !engine.answeredCurrent && !engine.isFinished
        nextButton.isEnabled = engine.answeredCurrent && !engine.isFinished
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        engine.snapshot().also {
            outState.putInt("first", it.first)
            outState.putInt("second", it.second)
            outState.putInt("answered", it.answered)
            outState.putInt("correct", it.correct)
            outState.putBoolean("answeredCurrent", it.answeredCurrent)
            outState.putBoolean("lastAnswerCorrect", it.lastAnswerCorrect)
        }
    }
}
