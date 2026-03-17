package com.grig.myanimelist.ui.animesearch

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.grig.core.theme.AppTheme
import com.grig.myanimelist.data.model.anime.MalAnime
import com.grig.myanimelist.ui.home.StatusBadge
import com.grig.myanimelist.ui.home.animeStatusColor
import com.grig.myanimelist.ui.home.previewAnime
import com.grig.myanimelist.ui.home.previewAnimeFinished
import com.grig.myanimelist.ui.home.previewAnimeList

@Composable
fun AnimeSearchResultsList(
    results: List<MalAnime>,
    onItemClick: (MalAnime) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(results, key = { it.id }) { anime ->
            AnimeSearchResultItem(anime = anime, onClick = { onItemClick(anime) })
        }
    }
}

@Composable
fun AnimeSearchResultItem(
    anime: MalAnime,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = anime.pictures?.medium,
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = anime.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val year = anime.startDate?.take(4)
                    if (year != null) {
                        Text(
                            text = year,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    StatusBadge(
                        text = anime.status.displayName,
                        color = animeStatusColor(anime.status)
                    )
                }
            }
        }
    }
}

@Preview(name = "Anime Search Result Item")
@Composable
private fun AnimeSearchResultItemPreview() {
    AppTheme(darkTheme = false) {
        AnimeSearchResultItem(anime = previewAnime, onClick = {})
    }
}

@Preview(name = "Anime Search Result Item - Finished")
@Composable
private fun AnimeSearchResultItemFinishedPreview() {
    AppTheme(darkTheme = false) {
        AnimeSearchResultItem(anime = previewAnimeFinished, onClick = {})
    }
}

@Preview(name = "Anime Search Results List")
@Composable
private fun AnimeSearchResultsListPreview() {
    AppTheme(darkTheme = false) {
        AnimeSearchResultsList(
            results = previewAnimeList,
            onItemClick = {}
        )
    }
}

@Preview(name = "Anime Search Result Item - Dark")
@Composable
private fun AnimeSearchResultItemDarkPreview() {
    AppTheme(darkTheme = true) {
        AnimeSearchResultItem(anime = previewAnime, onClick = {})
    }
}
