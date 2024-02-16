package com.grig.myanimelist.ui.anime

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.grig.myanimelist.ui.MalEmpty
import com.grig.myanimelist.ui.MalError
import com.grig.myanimelist.ui.MalLoading

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeListScreen(
    viewModel: AnimeListViewModel
) {
    val state = viewModel.state.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "MyAnimeList")
                }
            )
        }
    ) { padding ->
        when (state) {
            is AnimeUiState.Content -> AnimeContent(
                modifier = Modifier.padding(padding),
                animes = state.animes
            )

            AnimeUiState.Empty -> MalEmpty()
            is AnimeUiState.Error -> MalError()
            AnimeUiState.Loading -> MalLoading()
        }
    }
}