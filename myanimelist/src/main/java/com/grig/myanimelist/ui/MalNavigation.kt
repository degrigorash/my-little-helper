package com.grig.myanimelist.ui

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.grig.myanimelist.ui.theme.MyAnimeListTheme

fun NavGraphBuilder.malNavigation() {
    composable("mal_list") {
        MyAnimeListTheme {
            UserListScreen(viewModel = hiltViewModel())
        }
    }
}