package com.grig.myanimelist.ui.scorestats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grig.core.theme.AppTheme
import com.grig.myanimelist.data.model.jikan.JikanScoreBucket

/** Scrollable body of the score statistics dialog, below the fixed header. */
@Composable
fun ScoreStatsContent(
    scores: List<JikanScoreBucket>,
    totalVotes: Int,
    statuses: List<StatusSlice>,
    totalMembers: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        ScoreDistributionSection(scores = scores, totalVotes = totalVotes)

        Spacer(modifier = Modifier.height(20.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        Spacer(modifier = Modifier.height(20.dp))

        StatusSummarySection(statuses = statuses, totalMembers = totalMembers)
    }
}

@Preview(name = "Score Stats Content")
@Composable
private fun ScoreStatsContentPreview() {
    AppTheme(darkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.surfaceContainerHigh) {
            ScoreStatsContent(
                scores = previewScoreBuckets,
                totalVotes = previewTotalVotes,
                statuses = previewStatusSlices,
                totalMembers = previewTotalMembers,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
    }
}

@Preview(name = "Score Stats Content - Dark")
@Composable
private fun ScoreStatsContentDarkPreview() {
    AppTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.surfaceContainerHigh) {
            ScoreStatsContent(
                scores = previewScoreBuckets,
                totalVotes = previewTotalVotes,
                statuses = previewStatusSlices,
                totalMembers = previewTotalMembers,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
    }
}
