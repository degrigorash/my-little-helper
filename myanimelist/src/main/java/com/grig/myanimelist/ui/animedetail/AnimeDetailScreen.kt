package com.grig.myanimelist.ui.animedetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.grig.core.theme.AppThemeExtended
import com.grig.myanimelist.R
import com.grig.myanimelist.ui.animesearch.AnimeDetailContent

@Composable
fun AnimeDetailScreen(
    viewModel: AnimeDetailViewModel,
    navigateBack: () -> Unit,
    onListChanged: () -> Unit = {},
    navigateToAnimeDetail: (Int) -> Unit = {},
    navigateToMangaDetail: (Int) -> Unit = {},
    navigateToReviews: (Int) -> Unit = {},
    navigateToCharacters: (Int) -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val colors = AppThemeExtended.colorScheme
    val authorized by produceState(initialValue = false) {
        value = viewModel.isAuthorized()
    }

    LaunchedEffect(Unit) {
        viewModel.state.collect { s ->
            if (s is AnimeDetailState.Content && s.listChanged) {
                onListChanged()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        listOf(colors.malCardStart, colors.malCardEnd)
                    )
                )
                .statusBarsPadding()
                .padding(horizontal = 4.dp, vertical = 8.dp)
        ) {
            IconButton(onClick = navigateBack) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = "Back",
                    tint = colors.cardText
                )
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            when (val currentState = state) {
                is AnimeDetailState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.Center)
                    )
                }
                is AnimeDetailState.Content -> {
                    AnimeDetailContent(
                        anime = currentState.anime,
                        authorized = authorized,
                        isInMyList = currentState.isInMyList,
                        isUpdatingList = currentState.isUpdatingList,
                        onAddToList = viewModel::addToMyList,
                        onDeleteFromList = viewModel::deleteFromMyList,
                        onRelatedAnimeClick = navigateToAnimeDetail,
                        relatedManga = currentState.relatedManga,
                        isLoadingRelatedManga = currentState.isLoadingRelatedManga,
                        onRelatedMangaClick = navigateToMangaDetail,
                        onReviewsClick = { navigateToReviews(currentState.anime.id) },
                        onCharactersClick = { navigateToCharacters(currentState.anime.id) }
                    )
                }
                is AnimeDetailState.Error -> {
                    Text(
                        text = currentState.message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(32.dp)
                    )
                }
            }
        }
    }
}
