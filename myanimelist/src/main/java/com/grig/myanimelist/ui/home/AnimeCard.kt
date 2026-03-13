package com.grig.myanimelist.ui.home

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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.grig.core.theme.AppTheme
import com.grig.myanimelist.data.model.MalNsfw
import com.grig.myanimelist.data.model.MalRating
import com.grig.myanimelist.data.model.anime.MalAnime

@Composable
fun AnimeList(animes: List<MalAnime>) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(animes, key = { it.id }) { anime ->
            AnimeCard(anime = anime)
        }
    }
}

@Composable
fun AnimeCard(anime: MalAnime) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            AsyncImage(
                model = anime.pictures?.medium,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .blur(
                        if (anime.nsfw == MalNsfw.Nsfw || anime.rating == MalRating.Rx) 10.dp else 0.dp
                    ),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = anime.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = buildAnimeSubtitle(anime),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatsRow(
                        mean = anime.mean,
                        rank = anime.rank,
                        members = anime.numListUsers
                    )
                    StatusBadge(
                        text = anime.status.displayName,
                        color = animeStatusColor(anime.status)
                    )
                }
            }
        }
    }
}

private fun buildAnimeSubtitle(anime: MalAnime): String {
    val parts = mutableListOf<String>()
    anime.numEpisodes?.let { if (it > 0) parts.add("$it Episodes") }
    anime.genres.firstOrNull()?.let { parts.add(it.name) }
    return parts.joinToString(" \u00B7 ")
}

@Preview(name = "Anime Card")
@Composable
private fun AnimeCardPreview() {
    AppTheme(darkTheme = false) {
        AnimeCard(anime = previewAnime)
    }
}

@Preview(name = "Anime Card - Finished")
@Composable
private fun AnimeCardFinishedPreview() {
    AppTheme(darkTheme = false) {
        AnimeCard(anime = previewAnimeFinished)
    }
}

@Preview(name = "Anime Card - Dark")
@Composable
private fun AnimeCardDarkPreview() {
    AppTheme(darkTheme = true) {
        AnimeCard(anime = previewAnime)
    }
}
