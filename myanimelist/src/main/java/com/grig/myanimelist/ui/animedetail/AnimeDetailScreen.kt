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
    navigateToAnimeDetail: (Int) -> Unit = {},
    navigateToMangaDetail: (Int) -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val colors = AppThemeExtended.colorScheme
    val authorized by produceState(initialValue = false) {
        value = viewModel.isAuthorized()
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
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.Center)
                    )
                }
                state.anime != null -> {
                    AnimeDetailContent(
                        anime = state.anime!!,
                        authorized = authorized,
                        isInMyList = state.isInMyList,
                        isUpdatingList = state.isUpdatingList,
                        onAddToList = viewModel::addToMyList,
                        onDeleteFromList = viewModel::deleteFromMyList,
                        onRelatedAnimeClick = navigateToAnimeDetail,
                        relatedManga = state.relatedManga,
                        isLoadingRelatedManga = state.isLoadingRelatedManga,
                        onRelatedMangaClick = navigateToMangaDetail
                    )
                }
                state.error != null -> {
                    Text(
                        text = state.error!!,
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
