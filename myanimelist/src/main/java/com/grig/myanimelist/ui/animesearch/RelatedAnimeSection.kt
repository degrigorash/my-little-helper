package com.grig.myanimelist.ui.animesearch

import androidx.compose.foundation.clickable
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
import com.grig.myanimelist.data.model.anime.MalRelatedAnime
import com.grig.myanimelist.ui.animelist.previewAnime
import com.grig.myanimelist.ui.animelist.previewAnimeFinished
import com.grig.myanimelist.ui.home.StatusBadge

@Composable
fun RelatedAnimeSection(
    relatedAnime: List<MalRelatedAnime>,
    onRelatedAnimeClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Related Anime",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        relatedAnime.forEach { related ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onRelatedAnimeClick(related.node.id) }
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
                    StatusBadge(
                        text = related.relationTypeFormatted,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }
    }
}

@Preview(name = "Related Anime")
@Composable
private fun RelatedAnimeSectionPreview() {
    AppTheme(darkTheme = false) {
        RelatedAnimeSection(
            relatedAnime = listOf(
                MalRelatedAnime(
                    node = previewAnime,
                    relationType = "sequel",
                    relationTypeFormatted = "Sequel"
                ),
                MalRelatedAnime(
                    node = previewAnimeFinished,
                    relationType = "prequel",
                    relationTypeFormatted = "Prequel"
                )
            ),
            onRelatedAnimeClick = {}
        )
    }
}

@Preview(name = "Related Anime - Dark")
@Composable
private fun RelatedAnimeSectionDarkPreview() {
    AppTheme(darkTheme = true) {
        RelatedAnimeSection(
            relatedAnime = listOf(
                MalRelatedAnime(
                    node = previewAnime,
                    relationType = "sequel",
                    relationTypeFormatted = "Sequel"
                )
            ),
            onRelatedAnimeClick = {}
        )
    }
}
