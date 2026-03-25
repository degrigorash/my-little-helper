package com.grig.myanimelist.ui.common

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
import androidx.compose.material3.CircularProgressIndicator
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
import com.grig.myanimelist.data.model.jikan.ResolvedRelation
import com.grig.myanimelist.ui.home.StatusBadge

@Composable
fun CrossMediaRelationsSection(
    title: String,
    relations: List<ResolvedRelation>,
    isLoading: Boolean,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isLoading && relations.isEmpty()) return

    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (isLoading) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
            }
        } else {
            relations.forEach { resolved ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onClick(resolved.malId) }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (resolved.imageUrl != null) {
                        AsyncImage(
                            model = resolved.imageUrl,
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
                            text = resolved.name,
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
                                text = resolved.relation,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            if (resolved.year != null) {
                                Text(
                                    text = resolved.year,
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
}

@Preview(name = "Cross-Media Relations")
@Composable
private fun CrossMediaRelationsSectionPreview() {
    AppTheme(darkTheme = false) {
        CrossMediaRelationsSection(
            title = "Related Manga",
            relations = listOf(
                ResolvedRelation(malId = 11, name = "Naruto", type = "manga", relation = "Adaptation", year = "1999"),
                ResolvedRelation(malId = 86129, name = "Naruto Hiden Series", type = "manga", relation = "Adaptation", year = "2015")
            ),
            isLoading = false,
            onClick = {}
        )
    }
}

@Preview(name = "Cross-Media Relations - Loading")
@Composable
private fun CrossMediaRelationsSectionLoadingPreview() {
    AppTheme(darkTheme = false) {
        CrossMediaRelationsSection(
            title = "Related Anime",
            relations = emptyList(),
            isLoading = true,
            onClick = {}
        )
    }
}

@Preview(name = "Cross-Media Relations - Dark")
@Composable
private fun CrossMediaRelationsSectionDarkPreview() {
    AppTheme(darkTheme = true) {
        CrossMediaRelationsSection(
            title = "Related Manga",
            relations = listOf(
                ResolvedRelation(malId = 11, name = "Naruto", type = "manga", relation = "Adaptation", year = "1999")
            ),
            isLoading = false,
            onClick = {}
        )
    }
}
