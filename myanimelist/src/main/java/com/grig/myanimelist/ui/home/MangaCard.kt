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
import com.grig.myanimelist.data.model.manga.MalManga

@Composable
fun MangaList(mangas: List<MalManga>) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(mangas, key = { it.id }) { manga ->
            MangaCard(manga = manga)
        }
    }
}

@Composable
fun MangaCard(manga: MalManga) {
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
                model = manga.pictures?.medium,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .blur(
                        if (manga.nsfw == MalNsfw.Nsfw) 10.dp else 0.dp
                    ),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = manga.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = buildMangaSubtitle(manga),
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
                        mean = manga.mean,
                        rank = manga.rank,
                        members = manga.numListUsers
                    )
                    StatusBadge(
                        text = manga.status.displayName,
                        color = mangaStatusColor(manga.status)
                    )
                }
            }
        }
    }
}

private fun buildMangaSubtitle(manga: MalManga): String {
    val parts = mutableListOf<String>()
    manga.numVolumes?.let { if (it > 0) parts.add("$it Volumes") }
        ?: manga.numChapters?.let { if (it > 0) parts.add("$it Chapters") }
    parts.add(manga.mediaType.displayName)
    return parts.joinToString(" \u00B7 ")
}

@Preview(name = "Manga Card")
@Composable
private fun MangaCardPreview() {
    AppTheme(darkTheme = false) {
        MangaCard(manga = previewManga)
    }
}

@Preview(name = "Manga Card - Finished")
@Composable
private fun MangaCardFinishedPreview() {
    AppTheme(darkTheme = false) {
        MangaCard(manga = previewMangaFinished)
    }
}

@Preview(name = "Manga Card - Dark")
@Composable
private fun MangaCardDarkPreview() {
    AppTheme(darkTheme = true) {
        MangaCard(manga = previewManga)
    }
}
