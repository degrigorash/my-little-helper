package com.grig.danish

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.grig.core.theme.DanishTheme
import com.grig.danish.ui.DanishHome
import com.grig.danish.ui.noun.NounLearnScreen

fun NavGraphBuilder.danishNavigation(navController: NavHostController) {
    composable<DanishRoute.DanishHome> {
        DanishTheme {
            DanishHome(
                viewModel = hiltViewModel(),
                navigateToNounLearn = { mode ->
                    navController.navigate(DanishRoute.NounLearn(mode = mode))
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
}
