package com.grig.myanimelist.ui.scorestats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grig.core.theme.AppTheme
import com.grig.myanimelist.data.model.jikan.JikanScoreBucket

/**
 * One histogram row: score label, bar, share of votes and raw vote count.
 * [fraction] is the bar length relative to the track (the largest bucket fills it).
 */
@Composable
fun ScoreBarRow(
    bucket: JikanScoreBucket,
    fraction: Float,
    modifier: Modifier = Modifier
) {
    val barShape = RoundedCornerShape(4.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {},
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = bucket.score.toString(),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.End,
            modifier = Modifier.width(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(12.dp)
                .clip(barShape)
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
        ) {
            if (bucket.votes > 0) {
                // Floor at 1% so a bucket with a handful of votes stays visible.
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(fraction.coerceIn(0.01f, 1f))
                        .clip(barShape)
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = String.format("%.1f%%", bucket.percentage),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.End,
            modifier = Modifier.width(44.dp)
        )
        Text(
            text = "%,d".format(bucket.votes),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.End,
            modifier = Modifier.width(56.dp)
        )
    }
}

@Preview(name = "Score Bar Row")
@Composable
private fun ScoreBarRowPreview() {
    AppTheme(darkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.surfaceContainerHigh) {
            ScoreBarRow(
                bucket = previewScoreBuckets[3],
                fraction = 0.19f,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(name = "Score Bar Row - Dark")
@Composable
private fun ScoreBarRowDarkPreview() {
    AppTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.surfaceContainerHigh) {
            ScoreBarRow(
                bucket = previewScoreBuckets[3],
                fraction = 0.19f,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
