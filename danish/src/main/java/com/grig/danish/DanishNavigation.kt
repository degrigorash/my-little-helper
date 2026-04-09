package com.grig.danish

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.grig.core.theme.DanishTheme
import com.grig.danish.ui.DanishHome
import com.grig.danish.ui.noun.learn.NounLearnScreen
import com.grig.danish.ui.noun.practice.NounPracticeScreen

fun NavGraphBuilder.danishNavigation(navController: NavHostController) {
    composable<DanishRoute.DanishHome> {
        DanishTheme {
            DanishHome(
                viewModel = hiltViewModel(),
                navigateToNounLearn = { mode, shuffled ->
                    navController.navigate(DanishRoute.NounLearn(mode = mode, shuffled = shuffled))
                },
                navigateToNounPractice = { mode, shuffled ->
                    navController.navigate(DanishRoute.NounPractice(mode = mode, shuffled = shuffled))
                }
            )
        }
    }
    composable<DanishRoute.NounLearn> {
        DanishTheme {
            NounLearnScreen(
                viewModel = hiltViewModel(),
                navigateBack = { navController.popBackStack() }
            )
        }
    }
    composable<DanishRoute.NounPractice> {
        DanishTheme {
            NounPracticeScreen(
                viewModel = hiltViewModel(),
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}
