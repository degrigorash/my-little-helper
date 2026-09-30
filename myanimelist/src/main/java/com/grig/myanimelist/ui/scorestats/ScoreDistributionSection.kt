package com.grig.myanimelist.ui.scorestats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grig.core.theme.AppTheme
import com.grig.myanimelist.data.model.jikan.JikanScoreBucket

@Composable
fun ScoreDistributionSection(
    scores: List<JikanScoreBucket>,
    totalVotes: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Score distribution",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "${"%,d".format(totalVotes)} votes",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (totalVotes == 0) {
            Text(
                text = "No scores yet",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            val maxVotes = scores.maxOf { it.votes }.toFloat()
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                scores.forEach { bucket ->
                    ScoreBarRow(
                        bucket = bucket,
                        fraction = bucket.votes / maxVotes
                    )
                }
            }
        }
    }
}

@Preview(name = "Score Distribution")
@Composable
private fun ScoreDistributionSectionPreview() {
    AppTheme(darkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.surfaceContainerHigh) {
            ScoreDistributionSection(
                scores = previewScoreBuckets,
                totalVotes = previewTotalVotes,
                modifier = Modifier.padding(24.dp)
            )
        }
    }
}

@Preview(name = "Score Distribution - Dark")
@Composable
private fun ScoreDistributionSectionDarkPreview() {
    AppTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.surfaceContainerHigh) {
            ScoreDistributionSection(
                scores = previewScoreBuckets,
                totalVotes = previewTotalVotes,
                modifier = Modifier.padding(24.dp)
            )
        }
    }
}
