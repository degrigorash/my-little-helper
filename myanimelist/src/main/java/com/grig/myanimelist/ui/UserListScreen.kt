package com.grig.myanimelist.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

@Composable
fun UserListScreen(
    viewModel: UserListViewModel
) {
    val animes = viewModel.animeList.collectAsState().value

    LazyColumn {
        items(animes) { anime ->
            AnimeItem(anime)
        }
    }
}