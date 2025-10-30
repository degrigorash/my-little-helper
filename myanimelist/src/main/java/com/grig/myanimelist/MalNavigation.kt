package com.grig.myanimelist

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.grig.myanimelist.ui.anime.AnimeListScreen
import com.grig.myanimelist.ui.home.MalHomeScreen
import com.grig.myanimelist.ui.manga.MangaListScreen
import com.grig.myanimelist.ui.theme.MyAnimeListTheme

fun NavGraphBuilder.malNavigation(
    navController: NavHostController
) {
    composable<MalRoute.MalHome> {
        MyAnimeListTheme {
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
        MyAnimeListTheme {
            AnimeListScreen(viewModel = hiltViewModel())
        }
    }
    composable<MalRoute.MalMangaList> {
        MyAnimeListTheme {
            MangaListScreen(viewModel = hiltViewModel())
        }
    }
}