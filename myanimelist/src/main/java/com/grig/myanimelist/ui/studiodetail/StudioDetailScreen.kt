package com.grig.myanimelist.ui.studiodetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.grig.core.theme.AppThemeExtended
import com.grig.myanimelist.ui.common.CollapsingImage
import com.grig.myanimelist.ui.common.IMAGE_MAX_HEIGHT
import com.grig.myanimelist.ui.common.StatusBarScrim
import com.grig.myanimelist.ui.common.ToolbarOverlay

@Composable
fun StudioDetailScreen(
    viewModel: StudioDetailViewModel,
    navigateBack: () -> Unit,
    navigateToAnimeDetail: (Int) -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val colors = AppThemeExtended.colorScheme

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(listOf(colors.gradientBackgroundTop, colors.gradientBackgroundBottom)))
    ) {
        when (val currentState = state) {
            is StudioDetailState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.Center)
                )
            }
            is StudioDetailState.Content -> {
                val producer = currentState.producer
                val imageUrl = producer.images?.jpg?.imageUrl
                val density = LocalDensity.current
                val statusBarHeight = WindowInsets.statusBars.getTop(density)
                val imageMaxHeightPx = with(density) { IMAGE_MAX_HEIGHT.toPx() } + statusBarHeight
                val imageSpacerHeight = with(density) { imageMaxHeightPx.toDp() }

                val listState = rememberLazyListState()

                val collapseProgress by remember {
                    derivedStateOf {
                        if (listState.firstVisibleItemIndex > 0) {
                            1f
                        } else {
                            (listState.firstVisibleItemScrollOffset / imageMaxHeightPx).coerceIn(0f, 1f)
                        }
                    }
                }

                val imageHeightPx by remember {
                    derivedStateOf {
                        if (listState.firstVisibleItemIndex > 0) {
                            0f
                        } else {
                            (imageMaxHeightPx - listState.firstVisibleItemScrollOffset).coerceAtLeast(0f)
                        }
                    }
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    StudioDetailContent(
                        producer = producer,
                        animeList = currentState.animeList,
                        isLoadingAnime = currentState.isLoadingAnime,
                        hasMoreAnime = currentState.hasMoreAnime,
                        isLoadingMore = currentState.isLoadingMore,
                        onLoadMore = viewModel::loadMore,
                        onAnimeClick = navigateToAnimeDetail,
                        listState = listState,
                        imageSpacerHeight = imageSpacerHeight,
                        titleAlpha = 1f - collapseProgress
                    )

                    CollapsingImage(
                        imageUrl = imageUrl,
                        visibleHeightPx = imageHeightPx,
                        fullHeightPx = imageMaxHeightPx,
                        onImageClick = {},
                        contentScale = ContentScale.Fit
                    )

                    StatusBarScrim(collapseProgress = collapseProgress)

                    ToolbarOverlay(
                        title = producer.name,
                        collapseProgress = collapseProgress,
                        onBackClick = navigateBack
                    )
                }
            }
            is StudioDetailState.Error -> {
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
