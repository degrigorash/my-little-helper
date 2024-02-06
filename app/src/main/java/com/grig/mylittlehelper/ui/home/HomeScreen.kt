package com.grig.mylittlehelper.ui.home

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navigateToAnimeList: () -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            MalHomeScreen(
                isDarkTheme = isDarkTheme,
                viewModel = viewModel,
                navigateToAnimeList = navigateToAnimeList
            )
        }
    }
}