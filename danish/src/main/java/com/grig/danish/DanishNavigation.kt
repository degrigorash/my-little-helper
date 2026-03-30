package com.grig.danish

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.grig.core.theme.AppTheme
import com.grig.danish.ui.DanishHome
import com.grig.danish.ui.noun.NounLearnScreen

fun NavGraphBuilder.danishNavigation(navController: NavHostController) {
    composable<DanishRoute.DanishHome> {
        AppTheme {
            DanishHome(
                viewModel = hiltViewModel(),
                navigateToNounLearn = {
                    navController.navigate(DanishRoute.NounLearn())
                }
            )
        }
    }
    composable<DanishRoute.NounLearn> {
        AppTheme {
            NounLearnScreen(
                viewModel = hiltViewModel(),
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}
