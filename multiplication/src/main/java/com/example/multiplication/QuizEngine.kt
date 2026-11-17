package com.example.multiplication

import kotlin.random.Random

data class QuizSnapshot(
    val first: Int,
    val second: Int,
    val answered: Int,
    val correct: Int,
    val answeredCurrent: Boolean,
    val lastAnswerCorrect: Boolean
)

enum class AnswerResult { CORRECT, WRONG, INVALID, ALREADY_ANSWERED, FINISHED }

class QuizEngine(
    val selectedFactor: Int? = null,
    private val random: Random = Random.Default,
    saved: QuizSnapshot? = null
) {
    init {
        require(selectedFactor == null || selectedFactor in 2..9)
    }

    var first = saved?.first ?: (selectedFactor ?: random.nextInt(2, 10))
        private set
    var second = saved?.second ?: random.nextInt(2, 10)
        private set
    var answered = saved?.answered ?: 0
        private set
    var correct = saved?.correct ?: 0
        private set
    var answeredCurrent = saved?.answeredCurrent ?: false
        private set
    var lastAnswerCorrect = saved?.lastAnswerCorrect ?: false
        private set

    val isFinished: Boolean get() = answered == 20
    val percent: Int get() = correct * 100 / 20

    fun submitAnswer(text: String): AnswerResult {
        if (isFinished) return AnswerResult.FINISHED
        if (answeredCurrent) return AnswerResult.ALREADY_ANSWERED
        val answer = text.trim().toIntOrNull() ?: return AnswerResult.INVALID

        answered++
        answeredCurrent = true
        lastAnswerCorrect = answer == first * second
        if (lastAnswerCorrect) correct++
        return if (lastAnswerCorrect) AnswerResult.CORRECT else AnswerResult.WRONG
    }

    fun nextQuestion(): Boolean {
        if (!answeredCurrent || isFinished) return false
        first = selectedFactor ?: random.nextInt(2, 10)
        second = random.nextInt(2, 10)
        answeredCurrent = false
        lastAnswerCorrect = false
        return true
    }

    fun snapshot() = QuizSnapshot(first, second, answered, correct, answeredCurrent, lastAnswerCorrect)
}
