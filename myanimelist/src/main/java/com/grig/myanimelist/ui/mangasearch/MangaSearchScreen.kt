package com.grig.myanimelist.ui.mangasearch

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.grig.myanimelist.ui.MalSearchBar

@Composable
fun MangaSearchScreen(
    viewModel: MangaSearchViewModel,
    navigateBack: () -> Unit,
    onListChanged: () -> Unit = {},
    navigateToAnimeDetail: (Int) -> Unit = {},
    navigateToReviews: (Int, String) -> Unit = { _, _ -> }
) {
    val state by viewModel.state.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val authorized by produceState(initialValue = false) {
        value = viewModel.isAuthorized()
    }

    LaunchedEffect(Unit) {
        viewModel.state.collect { s ->
            if (s.listChanged) {
                onListChanged()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MalSearchBar(
            query = state.query,
            onQueryChange = viewModel::onQueryChange,
            onBack = navigateBack,
            placeholder = "Search manga..."
        )

        AnimatedVisibility(visible = state is MangaSearchState.Searching) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            when (val currentState = state) {
                is MangaSearchState.LoadingDetail -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.Center)
                    )
                }
                is MangaSearchState.Detail -> {
                    MangaDetailContent(
                        manga = currentState.manga,
                        authorized = authorized,
                        isInMyList = currentState.isInMyList,
                        isUpdatingList = currentState.isUpdatingList,
                        onAddToList = viewModel::addToMyList,
                        onDeleteFromList = viewModel::deleteFromMyList,
                        onRelatedMangaClick = viewModel::onMangaSelected,
                        relatedAnime = currentState.relatedAnime,
                        isLoadingRelatedAnime = currentState.isLoadingRelatedAnime,
                        onRelatedAnimeClick = navigateToAnimeDetail,
                        onReviewsClick = { navigateToReviews(currentState.manga.id, "manga") }
                    )
                }
                is MangaSearchState.Searching -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.Center)
                    )
                }
                is MangaSearchState.Results -> {
                    MangaSearchResultsList(
                        results = currentState.results,
                        onItemClick = {
                            keyboardController?.hide()
                            viewModel.onMangaSelected(it.id)
                        }
                    )
                }
                is MangaSearchState.Error -> {
                    Text(
                        text = currentState.message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(32.dp)
                    )
                }
                is MangaSearchState.NoResults -> {
                    Text(
                        text = "No results found",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(32.dp)
                    )
                }
                is MangaSearchState.Idle -> {}
            }
        }
    }
}
