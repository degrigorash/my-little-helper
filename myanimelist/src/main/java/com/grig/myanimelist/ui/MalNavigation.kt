package com.grig.myanimelist.ui

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.malNavigation() {
    composable("mal_list") {
        UserListScreen(viewModel = hiltViewModel())
    }
}