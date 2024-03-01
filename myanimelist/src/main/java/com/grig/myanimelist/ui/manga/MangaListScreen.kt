package com.grig.myanimelist.ui.manga

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.grig.myanimelist.ui.anime.AnimeContent
import com.grig.myanimelist.ui.anime.AnimeUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MangaListScreen(
    viewModel: MangaListViewModel
) {
    val state = viewModel.state.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "MyMangaList")
                }
            )
        }
    ) { padding ->
        when (state) {
            is MangaUiState.Content -> MangaContent(
                modifier = Modifier.padding(padding),
                mangas = state.mangas
            )

            MangaUiState.Empty -> MalEmpty()
            is MangaUiState.Error -> MalError()
            MangaUiState.Loading -> MalLoading()
        }
    }
}