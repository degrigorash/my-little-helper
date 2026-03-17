package com.grig.myanimelist

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.grig.core.theme.AppTheme
import com.grig.myanimelist.ui.animesearch.AnimeSearchScreen
import com.grig.myanimelist.ui.home.MalHomeScreen
import com.grig.myanimelist.ui.login.MalLoginScreen
import com.grig.myanimelist.ui.mangasearch.MangaSearchScreen

fun NavGraphBuilder.malNavigation(
    navController: NavHostController
) {
    composable<MalRoute.MalLogin> {
        AppTheme {
            MalLoginScreen(
                viewModel = hiltViewModel(),
                navigateToHome = {
                    navController.navigate(MalRoute.MalHome) {
                        popUpTo<MalRoute.MalLogin> { inclusive = true }
                    }
                },
                navigateBack = { navController.popBackStack() }
            )
        }
    }
    composable<MalRoute.MalHome> {
        AppTheme {
            MalHomeScreen(
                viewModel = hiltViewModel(),
                navigateToLogin = {
                    navController.navigate(MalRoute.MalLogin) {
                        popUpTo<MalRoute.MalHome> { inclusive = true }
                    }
                },
                navigateToAnimeSearch = {
                    navController.navigate(MalRoute.AnimeSearch)
                },
                navigateToMangaSearch = {
                    navController.navigate(MalRoute.MangaSearch)
                }
            )
        }
    }
    composable<MalRoute.AnimeSearch> {
        AppTheme {
            AnimeSearchScreen(
                viewModel = hiltViewModel(),
                navigateBack = { navController.popBackStack() }
            )
        }
    }
    composable<MalRoute.MangaSearch> {
        AppTheme {
            MangaSearchScreen(
                viewModel = hiltViewModel(),
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}
