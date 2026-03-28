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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
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
    onAnimeClick: (Int) -> Unit
) {
    val listState = rememberLazyListState()

    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = listState.layoutInfo.totalItemsCount
            lastVisibleItem >= totalItems - 5 && hasMoreAnime && !isLoadingMore
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) onLoadMore()
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            StudioInfoHeader(producer = producer)
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
        if (hasMoreAnime && !isLoadingMore && !isLoadingAnime) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = onLoadMore,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Load more")
                    }
                }
            }
        }
    }
}

@Composable
private fun StudioInfoHeader(producer: JikanProducer) {
    Column {
        val imageUrl = producer.images?.jpg?.imageUrl
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = producer.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Text(
            text = producer.name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
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
