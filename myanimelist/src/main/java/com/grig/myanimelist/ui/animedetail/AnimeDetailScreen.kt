package com.grig.myanimelist.ui.animedetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.grig.core.theme.AppThemeExtended
import com.grig.myanimelist.ui.animesearch.AnimeDetailContent
import com.grig.myanimelist.ui.common.CollapsingHeaderLayout
import com.grig.myanimelist.ui.common.FullscreenImageViewer

@Composable
fun AnimeDetailScreen(
    viewModel: AnimeDetailViewModel,
    navigateBack: () -> Unit,
    onListChanged: () -> Unit = {},
    navigateToAnimeDetail: (Int) -> Unit = {},
    navigateToMangaDetail: (Int) -> Unit = {},
    navigateToStudioDetail: (Int) -> Unit = {},
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(listOf(colors.gradientBackgroundTop, colors.gradientBackgroundBottom)))
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
                val anime = currentState.anime
                val imageUrl = anime.pictures?.let { it.large ?: it.medium }
                val galleryUrls = remember(anime) {
                    val rest = anime.gallery.mapNotNull { it.large ?: it.medium }
                    if (imageUrl != null) {
                        listOf(imageUrl) + rest.filter { it != imageUrl }
                    } else {
                        rest
                    }
                }
                var showFullscreenImage by remember { mutableStateOf(false) }

                CollapsingHeaderLayout(
                    title = anime.title,
                    imageUrl = imageUrl,
                    onBackClick = navigateBack,
                    onImageClick = { if (galleryUrls.isNotEmpty()) showFullscreenImage = true }
                ) { titleAlpha ->
                    AnimeDetailContent(
                        anime = anime,
                        authorized = authorized,
                        isInMyList = currentState.isInMyList,
                        isUpdatingList = currentState.isUpdatingList,
                        onAddToList = viewModel::addToMyList,
                        onDeleteFromList = viewModel::deleteFromMyList,
                        titleAlpha = titleAlpha,
                        onStudioClick = navigateToStudioDetail,
                        onRelatedAnimeClick = navigateToAnimeDetail,
                        relatedManga = currentState.relatedManga,
                        isLoadingRelatedManga = currentState.isLoadingRelatedManga,
                        onRelatedMangaClick = navigateToMangaDetail,
                        onReviewsClick = { navigateToReviews(anime.id) },
                        onCharactersClick = { navigateToCharacters(anime.id) }
                    )
                }

                if (showFullscreenImage && galleryUrls.isNotEmpty()) {
                    FullscreenImageViewer(
                        imageUrls = galleryUrls,
                        onDismiss = { showFullscreenImage = false }
                    )
                }
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
