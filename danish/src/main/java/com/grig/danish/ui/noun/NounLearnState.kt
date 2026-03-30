package com.grig.danish.ui.noun

import com.grig.danish.data.model.Noun

sealed interface NounLearnState {

    data object Loading : NounLearnState

    data class Content(val noun: Noun) : NounLearnState

    data class Error(val message: String) : NounLearnState
}
