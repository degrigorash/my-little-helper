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

@Composable
fun StatusSummarySection(
    statuses: List<StatusSlice>,
    totalMembers: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "List statistics",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "${"%,d".format(totalMembers)} members",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        StatusStackedBar(statuses = statuses, totalMembers = totalMembers)

        Spacer(modifier = Modifier.height(12.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            statuses.forEach { slice ->
                StatusCountRow(slice = slice, totalMembers = totalMembers)
            }
        }
    }
}

@Preview(name = "Status Summary")
@Composable
private fun StatusSummarySectionPreview() {
    AppTheme(darkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.surfaceContainerHigh) {
            StatusSummarySection(
                statuses = previewStatusSlices,
                totalMembers = previewTotalMembers,
                modifier = Modifier.padding(24.dp)
            )
        }
    }
}

@Preview(name = "Status Summary - Dark")
@Composable
private fun StatusSummarySectionDarkPreview() {
    AppTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.surfaceContainerHigh) {
            StatusSummarySection(
                statuses = previewStatusSlices,
                totalMembers = previewTotalMembers,
                modifier = Modifier.padding(24.dp)
            )
        }
    }
}
