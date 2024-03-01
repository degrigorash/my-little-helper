package com.grig.myanimelist.ui

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.grig.myanimelist.ui.anime.AnimeListScreen
import com.grig.myanimelist.ui.manga.MangaListScreen
import com.grig.myanimelist.ui.theme.MyAnimeListTheme

fun NavGraphBuilder.malNavigation() {
    composable("mal_anime_list/{username}") {
        MyAnimeListTheme {
            AnimeListScreen(viewModel = hiltViewModel())
        }
    }
    composable("mal_manga_list/{username}") {
        MyAnimeListTheme {
            MangaListScreen(viewModel = hiltViewModel())
        }
    }
}