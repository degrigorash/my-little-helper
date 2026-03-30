package com.grig.danish.ui.noun

import com.grig.danish.data.model.Noun
import com.grig.danish.ui.LearnMode

sealed interface NounLearnState {

    data object Loading : NounLearnState

    data class Content(
        val noun: Noun,
        val mode: LearnMode,
        val revealed: Boolean = false
    ) : NounLearnState

    data class Error(val message: String) : NounLearnState
}
