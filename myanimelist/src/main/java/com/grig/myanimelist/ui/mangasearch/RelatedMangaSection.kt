package com.grig.myanimelist.ui.mangasearch

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.grig.core.theme.AppTheme
import com.grig.myanimelist.data.model.manga.MalRelatedManga
import com.grig.myanimelist.ui.home.StatusBadge
import com.grig.myanimelist.ui.mangalist.previewManga
import com.grig.myanimelist.ui.mangalist.previewMangaFinished

@Composable
fun RelatedMangaSection(
    relatedManga: List<MalRelatedManga>,
    onRelatedMangaClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Related Manga",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        relatedManga.forEach { related ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onRelatedMangaClick(related.node.id) }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (related.node.pictures != null) {
                    AsyncImage(
                        model = related.node.pictures.medium
                            ?: related.node.pictures.large,
                        contentDescription = null,
                        modifier = Modifier
                            .size(width = 48.dp, height = 64.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = related.node.title,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StatusBadge(
                            text = related.relationTypeFormatted,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                        val year = related.node.startDate?.take(4)
                        if (year != null) {
                            Text(
                                text = year,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "Related Manga")
@Composable
private fun RelatedMangaSectionPreview() {
    AppTheme(darkTheme = false) {
        RelatedMangaSection(
            relatedManga = listOf(
                MalRelatedManga(
                    node = previewManga,
                    relationType = "sequel",
                    relationTypeFormatted = "Sequel"
                ),
                MalRelatedManga(
                    node = previewMangaFinished,
                    relationType = "prequel",
                    relationTypeFormatted = "Prequel"
                )
            ),
            onRelatedMangaClick = {}
        )
    }
}

@Preview(name = "Related Manga - Dark")
@Composable
private fun RelatedMangaSectionDarkPreview() {
    AppTheme(darkTheme = true) {
        RelatedMangaSection(
            relatedManga = listOf(
                MalRelatedManga(
                    node = previewManga,
                    relationType = "sequel",
                    relationTypeFormatted = "Sequel"
                )
            ),
            onRelatedMangaClick = {}
        )
    }
}
