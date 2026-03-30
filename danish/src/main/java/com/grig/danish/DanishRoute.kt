package com.grig.danish

import com.grig.danish.ui.LearnMode
import kotlinx.serialization.Serializable

sealed class DanishRoute {

    @Serializable
    data object DanishHome : DanishRoute()

    @Serializable
    data class NounLearn(
        val folder: String? = null,
        val mode: LearnMode = LearnMode.EN_TO_DK,
        val shuffled: Boolean = true
    ) : DanishRoute()

    @Serializable
    data class NounPractice(
        val folder: String? = null,
        val mode: LearnMode = LearnMode.EN_TO_DK,
        val shuffled: Boolean = true
    ) : DanishRoute()
}
