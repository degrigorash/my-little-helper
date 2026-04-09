package com.grig.danish.ui.noun.practice

import com.grig.danish.data.model.Noun
import com.grig.danish.ui.LearnMode

enum class AnswerResult { UNANSWERED, CORRECT, INCORRECT }

sealed interface NounPracticeState {

    data object Loading : NounPracticeState

    data class Content(
        val noun: Noun,
        val mode: LearnMode,
        val shuffled: Boolean = true,
        val progress: String = "",
        val userAnswer: String = "",
        val answerResult: AnswerResult = AnswerResult.UNANSWERED,
    ) : NounPracticeState

    data class Error(val message: String) : NounPracticeState
}
