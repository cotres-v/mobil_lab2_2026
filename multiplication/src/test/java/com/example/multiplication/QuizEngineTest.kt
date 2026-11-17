package com.example.multiplication

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class QuizEngineTest {
    @Test fun initialStateAndFactorRanges() {
        val quiz = QuizEngine(random = Random(1))
        assertEquals(0, quiz.answered)
        assertEquals(0, quiz.correct)
        assertTrue(quiz.first in 2..9)
        assertTrue(quiz.second in 2..9)
    }

    @Test fun selectedFactorIsFixedForTwentyQuestions() {
        val quiz = QuizEngine(selectedFactor = 7, random = Random(2))
        repeat(20) { index ->
            assertEquals(7, quiz.first)
            assertTrue(quiz.second in 2..9)
            quiz.submitAnswer("0")
            if (index < 19) assertTrue(quiz.nextQuestion())
        }
    }

    @Test fun wrongAnswerDoesNotIncreaseScore() {
        val quiz = QuizEngine(random = Random(3))
        assertEquals(AnswerResult.WRONG, quiz.submitAnswer("0"))
        assertEquals(1, quiz.answered)
        assertEquals(0, quiz.correct)
    }

    @Test fun invalidInputIsNotCounted() {
        val quiz = QuizEngine(random = Random(4))
        assertEquals(AnswerResult.INVALID, quiz.submitAnswer(""))
        assertEquals(AnswerResult.INVALID, quiz.submitAnswer("abc"))
        assertEquals(0, quiz.answered)
    }

    @Test fun duplicateTapIsNotCountedTwice() {
        val quiz = QuizEngine(random = Random(5))
        quiz.submitAnswer((quiz.first * quiz.second).toString())
        assertEquals(AnswerResult.ALREADY_ANSWERED, quiz.submitAnswer("0"))
        assertEquals(1, quiz.answered)
        assertEquals(1, quiz.correct)
    }

    @Test fun resultIsFiftyPercentAfterTenCorrectOfTwenty() {
        val quiz = QuizEngine(random = Random(6))
        repeat(20) { index ->
            val answer = quiz.first * quiz.second + if (index < 10) 0 else 1
            quiz.submitAnswer(answer.toString())
            if (index < 19) quiz.nextQuestion()
        }
        assertEquals(10, quiz.correct)
        assertEquals(50, quiz.percent)
    }

    @Test fun twentyFirstAnswerIsRejected() {
        val quiz = QuizEngine(random = Random(7))
        repeat(20) { index ->
            quiz.submitAnswer("0")
            if (index < 19) quiz.nextQuestion()
        }
        assertTrue(quiz.isFinished)
        assertEquals(AnswerResult.FINISHED, quiz.submitAnswer("0"))
        assertFalse(quiz.nextQuestion())
        assertEquals(20, quiz.answered)
    }

    @Test fun snapshotPreservesProgress() {
        val quiz = QuizEngine(selectedFactor = 5, random = Random(8))
        quiz.submitAnswer((quiz.first * quiz.second).toString())
        quiz.nextQuestion()
        val saved = quiz.snapshot()
        val restored = QuizEngine(selectedFactor = 5, random = Random(9), saved = saved)
        assertEquals(saved, restored.snapshot())
        assertEquals(1, restored.answered)
        assertEquals(1, restored.correct)
    }

    @Test(expected = IllegalArgumentException::class)
    fun selectedNumberOutsideRangeIsRejected() {
        QuizEngine(selectedFactor = 10)
    }
}
