package com.grig.myanimelist.ui.scorestats

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.grig.myanimelist.ui.common.MalErrorContent

/**
 * Score distribution and list-status breakdown for an anime or manga, opened by tapping
 * the score on a detail screen. Hosted as a navigation `dialog` destination — the Compose
 * counterpart of a DialogFragment — so it has its own back-stack entry and ViewModel;
 * back press and outside taps dismiss it through the NavHost.
 */
@Composable
fun ScoreStatsScreen(
    viewModel: ScoreStatsViewModel,
    onDismiss: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Surface(
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        Column(modifier = Modifier.padding(top = 12.dp, bottom = 24.dp)) {
            ScoreStatsHeader(
                mean = viewModel.mean,
                onClose = onDismiss,
                modifier = Modifier.padding(start = 24.dp, end = 12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            when (val currentState = state) {
                is ScoreStatsState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(48.dp))
                    }
                }
                is ScoreStatsState.Error -> {
                    MalErrorContent(
                        title = "Couldn't load statistics",
                        description = currentState.message,
                        onRetry = viewModel::retry,
                        modifier = Modifier.height(360.dp)
                    )
                }
                is ScoreStatsState.Empty -> {
                    Text(
                        text = "No statistics yet",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp)
                    )
                }
                is ScoreStatsState.Content -> {
                    ScoreStatsContent(
                        scores = currentState.scores,
                        totalVotes = currentState.totalVotes,
                        statuses = currentState.statuses,
                        totalMembers = currentState.totalMembers,
                        // Bounded by the remaining dialog height so the body scrolls
                        // under the fixed header on short screens.
                        modifier = Modifier.weight(1f, fill = false)
                    )
                }
            }
        }
    }
}
