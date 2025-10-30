package com.grig.danish

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.grig.danish.ui.theme.DanishTheme
import com.grig.danish.ui.learn.LearnNounRoute
import com.grig.danish.ui.quiz.VocabularyQuizRoute

fun NavGraphBuilder.danishNavigation() {
    composable<DanishRoute.DanishHome> {
        DanishTheme { LearnNounRoute() }
    }
    composable<DanishRoute.DanishLearnNoun> {
        DanishTheme { LearnNounRoute() }
    }
    composable<DanishRoute.DanishQuizNoun> {
        DanishTheme { VocabularyQuizRoute() }
    }
}