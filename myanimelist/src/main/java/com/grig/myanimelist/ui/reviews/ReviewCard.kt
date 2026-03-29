package com.grig.myanimelist.ui.reviews

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.grig.core.theme.AppTheme
import com.grig.core.theme.AppThemeExtended
import com.grig.myanimelist.data.model.jikan.JikanReview
import com.grig.myanimelist.ui.home.StatusBadge
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ReviewCard(review: JikanReview) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val avatarUrl = review.user.images?.jpg?.imageUrl
                ?: review.user.images?.webp?.imageUrl
            if (avatarUrl != null) {
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(10.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = review.user.username,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = formatReviewDate(review.date),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            ScoreBadge(score = review.score)
        }

        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            review.tags.forEach { tag ->
                StatusBadge(
                    text = tag,
                    color = tagColor(tag)
                )
            }
            if (review.isPreliminary) {
                StatusBadge(
                    text = "Preliminary",
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            if (review.isSpoiler) {
                StatusBadge(
                    text = "Spoiler",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = review.review,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = if (expanded) Int.MAX_VALUE else 5,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.animateContentSize()
        )

        val reactionsTotal = review.reactions?.overall ?: 0
        if (reactionsTotal > 0) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "$reactionsTotal reactions",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ScoreBadge(score: Int) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = scoreColor(score),
        tonalElevation = 0.dp
    ) {
        Text(
            text = score.toString(),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun scoreColor(score: Int): Color {
    val colors = AppThemeExtended.colorScheme
    return when {
        score >= 8 -> colors.scoreHigh
        score >= 5 -> colors.scoreMid
        else -> colors.scoreLow
    }
}

@Composable
fun tagColor(tag: String): Color = when (tag) {
    "Recommended" -> MaterialTheme.colorScheme.primary
    "Not Recommended" -> MaterialTheme.colorScheme.error
    else -> MaterialTheme.colorScheme.secondary
}

fun formatReviewDate(isoDate: String): String {
    return try {
        val parsed = ZonedDateTime.parse(isoDate)
        parsed.format(DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH))
    } catch (_: Exception) {
        isoDate.take(10)
    }
}

@Preview(name = "Review Card")
@Composable
private fun ReviewCardPreview() {
    AppTheme(darkTheme = false) {
        ReviewCard(review = previewReview)
    }
}

@Preview(name = "Review Card - Dark")
@Composable
private fun ReviewCardDarkPreview() {
    AppTheme(darkTheme = true) {
        ReviewCard(review = previewReview)
    }
}

@Preview(name = "Score Badge - High")
@Composable
private fun ScoreBadgeHighPreview() {
    AppTheme(darkTheme = false) {
        ScoreBadge(score = 9)
    }
}

@Preview(name = "Score Badge - Low")
@Composable
private fun ScoreBadgeLowPreview() {
    AppTheme(darkTheme = false) {
        ScoreBadge(score = 3)
    }
}
