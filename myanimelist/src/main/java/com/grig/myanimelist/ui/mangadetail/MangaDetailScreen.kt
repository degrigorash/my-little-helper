package com.grig.myanimelist.ui.mangadetail

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
import com.grig.myanimelist.ui.common.CollapsingHeaderLayout
import com.grig.myanimelist.ui.common.FullscreenImageViewer
import com.grig.myanimelist.ui.mangasearch.MangaDetailContent

@Composable
fun MangaDetailScreen(
    viewModel: MangaDetailViewModel,
    navigateBack: () -> Unit,
    onListChanged: () -> Unit = {},
    navigateToMangaDetail: (Int) -> Unit = {},
    navigateToAnimeDetail: (Int) -> Unit = {},
    navigateToAuthorDetail: (Int) -> Unit = {},
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
            if (s is MangaDetailState.Content && s.listChanged) {
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
            is MangaDetailState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.Center)
                )
            }
            is MangaDetailState.Content -> {
                val manga = currentState.manga
                val imageUrl = manga.pictures?.let { it.large ?: it.medium }
                val galleryUrls = remember(manga) {
                    val rest = manga.gallery.mapNotNull { it.large ?: it.medium }
                    if (imageUrl != null) {
                        listOf(imageUrl) + rest.filter { it != imageUrl }
                    } else {
                        rest
                    }
                }
                var showFullscreenImage by remember { mutableStateOf(false) }

                CollapsingHeaderLayout(
                    title = manga.title,
                    imageUrl = imageUrl,
                    onBackClick = navigateBack,
                    onImageClick = { if (galleryUrls.isNotEmpty()) showFullscreenImage = true }
                ) { titleAlpha ->
                    MangaDetailContent(
                        manga = manga,
                        authorized = authorized,
                        isInMyList = currentState.isInMyList,
                        isUpdatingList = currentState.isUpdatingList,
                        onAddToList = viewModel::addToMyList,
                        onDeleteFromList = viewModel::deleteFromMyList,
                        titleAlpha = titleAlpha,
                        onAuthorClick = navigateToAuthorDetail,
                        onRelatedMangaClick = navigateToMangaDetail,
                        relatedAnime = currentState.relatedAnime,
                        isLoadingRelatedAnime = currentState.isLoadingRelatedAnime,
                        onRelatedAnimeClick = navigateToAnimeDetail,
                        onReviewsClick = { navigateToReviews(manga.id) },
                        onCharactersClick = { navigateToCharacters(manga.id) }
                    )
                }

                if (showFullscreenImage && galleryUrls.isNotEmpty()) {
                    FullscreenImageViewer(
                        imageUrls = galleryUrls,
                        onDismiss = { showFullscreenImage = false }
                    )
                }
            }
            is MangaDetailState.Error -> {
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
