package com.grig.danish.ui.quiz

sealed class QuizState(open val word: String) {

    data object Loading : QuizState("")

    data class Word(
        override val word: String
    ) : QuizState(word)

    data class Correct(
        override val word: String,
        val link: String
    ) : QuizState(word)

    data class Wrong(
        override val word: String,
        val correct: String,
        val link: String
    ) : QuizState(word)
}