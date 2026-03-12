package com.grig.myanimelist

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.grig.core.theme.AppTheme
import com.grig.myanimelist.ui.anime.AnimeListScreen
import com.grig.myanimelist.ui.home.MalHomeScreen
import com.grig.myanimelist.ui.login.MalLoginScreen
import com.grig.myanimelist.ui.manga.MangaListScreen

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
                navigateToAnimeList = { username ->
                    navController.navigate(MalRoute.MalAnimeList(username))
                },
                navigateToMangaList = { username ->
                    navController.navigate(MalRoute.MalMangaList(username))
                }
            )
        }
    }
    composable<MalRoute.MalAnimeList> {
        AppTheme {
            AnimeListScreen(viewModel = hiltViewModel())
        }
    }
    composable<MalRoute.MalMangaList> {
        AppTheme {
            MangaListScreen(viewModel = hiltViewModel())
        }
    }
}