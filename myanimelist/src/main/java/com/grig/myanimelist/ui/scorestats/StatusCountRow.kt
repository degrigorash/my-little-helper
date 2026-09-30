package com.grig.myanimelist.ui.scorestats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grig.core.theme.AppTheme

/** Legend row for [StatusStackedBar]: color dot, status, member count and share. */
@Composable
fun StatusCountRow(
    slice: StatusSlice,
    totalMembers: Int,
    modifier: Modifier = Modifier
) {
    val share = if (totalMembers > 0) slice.count * 100f / totalMembers else 0f

    Row(
        modifier = modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {},
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(slice.color, CircleShape)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = slice.label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "%,d".format(slice.count),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = String.format("%.1f%%", share),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.End,
            modifier = Modifier.width(56.dp)
        )
    }
}

@Preview(name = "Status Count Row")
@Composable
private fun StatusCountRowPreview() {
    AppTheme(darkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.surfaceContainerHigh) {
            StatusCountRow(
                slice = previewStatusSlices.first(),
                totalMembers = previewTotalMembers,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(name = "Status Count Row - Dark")
@Composable
private fun StatusCountRowDarkPreview() {
    AppTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.surfaceContainerHigh) {
            StatusCountRow(
                slice = previewStatusSlices.first(),
                totalMembers = previewTotalMembers,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
