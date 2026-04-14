package com.grig.myanimelist.ui.studiodetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.grig.core.theme.AppTheme
import com.grig.myanimelist.data.model.jikan.JikanAnimeListItem
import com.grig.myanimelist.data.model.jikan.JikanProducer

@Composable
fun StudioDetailContent(
    producer: JikanProducer,
    animeList: List<JikanAnimeListItem>,
    isLoadingAnime: Boolean,
    hasMoreAnime: Boolean,
    isLoadingMore: Boolean,
    onLoadMore: () -> Unit,
    onAnimeClick: (Int) -> Unit,
    listState: LazyListState = rememberLazyListState(),
    imageSpacerHeight: Dp = 0.dp,
    titleAlpha: Float = 1f
) {
    val nearEnd by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = listState.layoutInfo.totalItemsCount
            lastVisibleItem >= totalItems - 5
        }
    }

    LaunchedEffect(nearEnd, hasMoreAnime, isLoadingMore) {
        if (nearEnd && hasMoreAnime && !isLoadingMore) onLoadMore()
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (imageSpacerHeight > 0.dp) {
            item(key = "image_spacer") {
                Spacer(modifier = Modifier.height(imageSpacerHeight))
            }
        }

        item(key = "studio_header") {
            StudioInfoHeader(producer = producer, titleAlpha = titleAlpha)
        }

        if (animeList.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Anime (${producer.count})",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            items(animeList, key = { it.malId }) { anime ->
                StudioAnimeItem(anime = anime, onClick = { onAnimeClick(anime.malId) })
            }
        }

        if (isLoadingAnime || isLoadingMore) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                }
            }
        }
    }
}

@Composable
private fun StudioInfoHeader(
    producer: JikanProducer,
    titleAlpha: Float = 1f
) {
    Column {
        Text(
            text = producer.name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.graphicsLayer { alpha = titleAlpha }
        )

        producer.japaneseName?.let { jpName ->
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = jpName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        val established = producer.established?.take(10)
        if (established != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Established: $established",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (producer.favorites > 0) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${producer.favorites} favorites",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (!producer.about.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = producer.about,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(name = "Studio Detail Content")
@Composable
private fun StudioDetailContentPreview() {
    AppTheme(darkTheme = false) {
        StudioDetailContent(
            producer = previewProducer,
            animeList = previewStudioAnimeList,
            isLoadingAnime = false,
            hasMoreAnime = true,
            isLoadingMore = false,
            onLoadMore = {},
            onAnimeClick = {}
        )
    }
}

@Preview(name = "Studio Detail Content - Dark")
@Composable
private fun StudioDetailContentDarkPreview() {
    AppTheme(darkTheme = true) {
        StudioDetailContent(
            producer = previewProducer,
            animeList = previewStudioAnimeList,
            isLoadingAnime = false,
            hasMoreAnime = false,
            isLoadingMore = false,
            onLoadMore = {},
            onAnimeClick = {}
        )
    }
}
