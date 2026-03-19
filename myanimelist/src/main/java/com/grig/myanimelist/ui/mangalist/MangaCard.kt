package com.grig.myanimelist.ui.mangalist

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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.grig.core.theme.AppTheme
import com.grig.myanimelist.R
import com.grig.myanimelist.ui.home.ListSearchBar
import com.grig.myanimelist.ui.home.StatusBadge
import com.grig.myanimelist.ui.home.buildAiredText
import com.grig.myanimelist.ui.home.formatMemberCount
import com.grig.myanimelist.ui.home.mangaStatusColor
import com.grig.myanimelist.ui.home.readingStatusColor

@Composable
fun MangaList(
    mangas: List<MangaCardData>,
    onMangaClick: ((MangaCardData) -> Unit)? = null,
    searchQuery: String = "",
    onSearchQueryChange: (String) -> Unit = {}
) {
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = 1)

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "search_bar") {
            ListSearchBar(
                query = searchQuery,
                onQueryChange = onSearchQueryChange
            )
        }
        items(mangas, key = { it.manga.id }) { data ->
            MangaCard(data = data, onClick = onMangaClick?.let { { it(data) } })
        }
    }
}

@Composable
fun MangaCard(data: MangaCardData, onClick: (() -> Unit)? = null) {
    val manga = data.manga
    val listStatus = data.listStatus

    Card(
        onClick = { onClick?.invoke() },
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
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = manga.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                val authorNames = manga.authors
                    .map { it.author.displayName }
                    .distinct()
                    .joinToString(", ")
                if (authorNames.isNotEmpty()) {
                    Text(
                        text = authorNames,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                val progressText = buildReadProgress(data)
                if (progressText != null) {
                    Text(
                        text = progressText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                val dateText = buildAiredText(manga.startDate, manga.endDate)
                if (dateText != null) {
                    Text(
                        text = dateText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                MangaStatsRow(
                    mean = manga.mean,
                    rank = manga.rank,
                    members = manga.numListUsers,
                    myScore = listStatus?.score
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (listStatus != null) {
                        StatusBadge(
                            text = listStatus.status.displayName,
                            color = readingStatusColor(listStatus.status)
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

@Composable
private fun MangaStatsRow(
    mean: Float?,
    rank: Int?,
    members: Int?,
    myScore: Int?
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (mean != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.ic_star),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFFFFC107)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = String.format("%.1f", mean),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        if (myScore != null && myScore > 0) {
            Text(
                text = "My: $myScore",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF7C4DFF)
            )
        }
        if (rank != null) {
            Text(
                text = "#$rank",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (members != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.ic_person),
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = formatMemberCount(members),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun buildReadProgress(data: MangaCardData): String? {
    val chaptersRead = data.listStatus?.numChaptersRead
    val totalChapters = data.manga.numChapters?.takeIf { it > 0 }
    val volumesRead = data.listStatus?.numVolumesRead
    val totalVolumes = data.manga.numVolumes?.takeIf { it > 0 }

    val parts = mutableListOf<String>()

    val chText = when {
        chaptersRead != null && totalChapters != null -> "$chaptersRead/$totalChapters ch"
        chaptersRead != null && chaptersRead > 0 -> "$chaptersRead ch read"
        totalChapters != null -> "$totalChapters ch"
        else -> null
    }
    if (chText != null) parts.add(chText)

    val volText = when {
        volumesRead != null && totalVolumes != null -> "$volumesRead/$totalVolumes vol"
        volumesRead != null && volumesRead > 0 -> "$volumesRead vol read"
        totalVolumes != null -> "$totalVolumes vol"
        else -> null
    }
    if (volText != null) parts.add(volText)

    return parts.joinToString(" \u00B7 ").ifEmpty { null }
}

@Preview(name = "Manga Card")
@Composable
private fun MangaCardPreview() {
    AppTheme(darkTheme = false) {
        MangaCard(data = previewMangaCardData)
    }
}

@Preview(name = "Manga Card - Finished")
@Composable
private fun MangaCardFinishedPreview() {
    AppTheme(darkTheme = false) {
        MangaCard(data = previewMangaCardDataFinished)
    }
}

@Preview(name = "Manga Card - Dark")
@Composable
private fun MangaCardDarkPreview() {
    AppTheme(darkTheme = true) {
        MangaCard(data = previewMangaCardData)
    }
}
