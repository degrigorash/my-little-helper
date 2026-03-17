package com.grig.myanimelist.ui.mangasearch

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
import com.grig.myanimelist.data.model.manga.MalManga
import com.grig.myanimelist.ui.home.StatusBadge
import com.grig.myanimelist.ui.home.mangaStatusColor
import com.grig.myanimelist.ui.home.previewManga
import com.grig.myanimelist.ui.home.previewMangaFinished
import com.grig.myanimelist.ui.home.previewMangaList

@Composable
fun MangaSearchResultsList(
    results: List<MalManga>,
    onItemClick: (MalManga) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(results, key = { it.id }) { manga ->
            MangaSearchResultItem(manga = manga, onClick = { onItemClick(manga) })
        }
    }
}

@Composable
fun MangaSearchResultItem(
    manga: MalManga,
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
                model = manga.pictures?.medium,
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = manga.title,
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
                    val year = manga.startDate?.take(4)
                    if (year != null) {
                        Text(
                            text = year,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    StatusBadge(
                        text = manga.status.displayName,
                        color = mangaStatusColor(manga.status)
                    )
                }
            }
        }
    }
}

@Preview(name = "Manga Search Result Item")
@Composable
private fun MangaSearchResultItemPreview() {
    AppTheme(darkTheme = false) {
        MangaSearchResultItem(manga = previewManga, onClick = {})
    }
}

@Preview(name = "Manga Search Result Item - Finished")
@Composable
private fun MangaSearchResultItemFinishedPreview() {
    AppTheme(darkTheme = false) {
        MangaSearchResultItem(manga = previewMangaFinished, onClick = {})
    }
}

@Preview(name = "Manga Search Results List")
@Composable
private fun MangaSearchResultsListPreview() {
    AppTheme(darkTheme = false) {
        MangaSearchResultsList(
            results = previewMangaList,
            onItemClick = {}
        )
    }
}

@Preview(name = "Manga Search Result Item - Dark")
@Composable
private fun MangaSearchResultItemDarkPreview() {
    AppTheme(darkTheme = true) {
        MangaSearchResultItem(manga = previewManga, onClick = {})
    }
}
