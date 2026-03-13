package com.grig.myanimelist.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grig.core.theme.AppTheme
import com.grig.myanimelist.R
import com.grig.myanimelist.data.model.anime.MalAnimeAiringStatus
import com.grig.myanimelist.data.model.anime.MalAnimeWatchingStatus
import com.grig.myanimelist.data.model.manga.MalMangaPublishStatus
import com.grig.myanimelist.data.model.manga.MalMangaReadingStatus

@Composable
fun StatsRow(
    mean: Float?,
    rank: Int?,
    members: Int?
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
                    fontWeight = FontWeight.SemiBold
                )
            }
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

@Composable
fun StatusBadge(text: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.15f)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = color,
            fontSize = 11.sp
        )
    }
}

fun animeStatusColor(status: MalAnimeAiringStatus): Color = when (status) {
    MalAnimeAiringStatus.CurrentlyAiring -> Color(0xFF4CAF50)
    MalAnimeAiringStatus.FinishedAiring -> Color(0xFF78909C)
    MalAnimeAiringStatus.NotYetAired -> Color(0xFF42A5F5)
}

fun watchingStatusColor(status: MalAnimeWatchingStatus): Color = when (status) {
    MalAnimeWatchingStatus.Watching -> Color(0xFF4CAF50)
    MalAnimeWatchingStatus.Completed -> Color(0xFF42A5F5)
    MalAnimeWatchingStatus.OnHold -> Color(0xFFFF9800)
    MalAnimeWatchingStatus.Dropped -> Color(0xFFE53935)
    MalAnimeWatchingStatus.PlanToWatch -> Color(0xFF78909C)
}

fun readingStatusColor(status: MalMangaReadingStatus): Color = when (status) {
    MalMangaReadingStatus.Reading -> Color(0xFF4CAF50)
    MalMangaReadingStatus.Completed -> Color(0xFF42A5F5)
    MalMangaReadingStatus.OnHold -> Color(0xFFFF9800)
    MalMangaReadingStatus.Dropped -> Color(0xFFE53935)
    MalMangaReadingStatus.PlanToRead -> Color(0xFF78909C)
}

fun mangaStatusColor(status: MalMangaPublishStatus): Color = when (status) {
    MalMangaPublishStatus.CurrentlyPublishing -> Color(0xFF4CAF50)
    MalMangaPublishStatus.Finished -> Color(0xFF78909C)
    MalMangaPublishStatus.NotYetPublished -> Color(0xFF42A5F5)
    MalMangaPublishStatus.OnHiatus -> Color(0xFFFF9800)
    MalMangaPublishStatus.Discontinued -> Color(0xFFE53935)
}

fun formatMemberCount(count: Int): String = when {
    count >= 1_000_000 -> String.format("%.1fM", count / 1_000_000.0)
    count >= 1_000 -> String.format("%.1fK", count / 1_000.0)
    else -> count.toString()
}

@Preview(name = "Stats Row")
@Composable
private fun StatsRowPreview() {
    AppTheme(darkTheme = false) {
        StatsRow(mean = 8.9f, rank = 1, members = 2_100_000)
    }
}

@Preview(name = "Status Badge - Airing")
@Composable
private fun StatusBadgeAiringPreview() {
    AppTheme(darkTheme = false) {
        StatusBadge(
            text = MalAnimeAiringStatus.CurrentlyAiring.displayName,
            color = animeStatusColor(MalAnimeAiringStatus.CurrentlyAiring)
        )
    }
}

@Preview(name = "Status Badge - Finished")
@Composable
private fun StatusBadgeFinishedPreview() {
    AppTheme(darkTheme = false) {
        StatusBadge(
            text = MalAnimeAiringStatus.FinishedAiring.displayName,
            color = animeStatusColor(MalAnimeAiringStatus.FinishedAiring)
        )
    }
}
