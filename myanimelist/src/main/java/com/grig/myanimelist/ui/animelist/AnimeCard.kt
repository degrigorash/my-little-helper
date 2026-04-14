package com.grig.myanimelist.ui.animelist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import com.grig.myanimelist.ui.home.FilteredEmptyItem
import com.grig.myanimelist.ui.home.ListSearchBar
import com.grig.myanimelist.ui.home.StatusBadge
import com.grig.myanimelist.ui.home.animeStatusColor
import com.grig.myanimelist.ui.home.buildAiredText
import com.grig.myanimelist.ui.home.formatMemberCount
import com.grig.myanimelist.ui.home.watchingStatusColor

@Composable
fun AnimeList(
    animes: List<AnimeCardData>,
    onAnimeClick: ((AnimeCardData) -> Unit)? = null,
    searchQuery: String = "",
    onSearchQueryChange: (String) -> Unit = {},
    watchlistIds: Set<Int> = emptySet()
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
        if (animes.isEmpty()) {
            item(key = "filtered_empty") {
                FilteredEmptyItem()
            }
        }
        items(animes, key = { it.anime.id }) { data ->
            AnimeCard(
                data = data,
                onClick = onAnimeClick?.let { { it(data) } },
                isBookmarked = data.anime.id in watchlistIds
            )
        }
    }
}

@Composable
fun AnimeCard(
    data: AnimeCardData,
    onClick: (() -> Unit)? = null,
    isBookmarked: Boolean = false
) {
    val anime = data.anime
    val listStatus = data.listStatus

    Card(
        onClick = { onClick?.invoke() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box {
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
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = anime.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    val studioNames = anime.studios.joinToString(", ") { it.name }
                    if (studioNames.isNotEmpty()) {
                        Text(
                            text = studioNames,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    val epText = buildEpisodeProgress(data)
                    if (epText != null) {
                        Text(
                            text = epText,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    val airedText = buildAiredText(anime.startDate, anime.endDate)
                    if (airedText != null) {
                        Text(
                            text = airedText,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    StatsRowWithMyScore(
                        mean = anime.mean,
                        rank = anime.rank,
                        members = anime.numListUsers,
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
                                color = watchingStatusColor(listStatus.status)
                            )
                        }
                        StatusBadge(
                            text = anime.status.displayName,
                            color = animeStatusColor(anime.status)
                        )
                    }
                }
            }
            if (isBookmarked) {
                Icon(
                    painter = painterResource(R.drawable.ic_bookmark),
                    contentDescription = "In Watch Next",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun StatsRowWithMyScore(
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

private fun buildEpisodeProgress(data: AnimeCardData): String? {
    val watched = data.listStatus?.numEpisodesWatched
    val total = data.anime.numEpisodes?.takeIf { it > 0 }
    return when {
        watched != null && total != null -> "$watched/$total episodes"
        watched != null -> "$watched episodes watched"
        total != null -> "$total episodes"
        else -> null
    }
}

@Preview(name = "Anime Card")
@Composable
private fun AnimeCardPreview() {
    AppTheme(darkTheme = false) {
        AnimeCard(data = previewAnimeCardData)
    }
}

@Preview(name = "Anime Card - Finished")
@Composable
private fun AnimeCardFinishedPreview() {
    AppTheme(darkTheme = false) {
        AnimeCard(data = previewAnimeCardDataFinished)
    }
}

@Preview(name = "Anime Card - Dark")
@Composable
private fun AnimeCardDarkPreview() {
    AppTheme(darkTheme = true) {
        AnimeCard(data = previewAnimeCardData)
    }
}
