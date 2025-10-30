package com.grig.danish

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.grig.core.theme.AppTheme
import com.grig.danish.ui.learn.LearnNounRoute
import com.grig.danish.ui.quiz.QuizRoute

fun NavGraphBuilder.danishNavigation() {
    composable<DanishRoute.DanishHome> {
        AppTheme { QuizRoute() }
    }
    composable<DanishRoute.DanishLearnNoun> {
        AppTheme { LearnNounRoute() }
    }
    composable<DanishRoute.DanishQuizNoun> {
        AppTheme { QuizRoute() }
    }
}