package com.grig.myanimelist.ui.scorestats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grig.core.theme.AppTheme

/**
 * Single horizontal bar split by list status. Decorative: the [StatusCountRow]
 * legend below carries every label and number.
 */
@Composable
fun StatusStackedBar(
    statuses: List<StatusSlice>,
    totalMembers: Int,
    modifier: Modifier = Modifier
) {
    if (totalMembers <= 0) return

    // Slivers under 0.5% would render as nothing but an extra 2dp gap.
    val visible = statuses.filter { it.count.toFloat() / totalMembers >= 0.005f }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(12.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        visible.forEach { slice ->
            Box(
                modifier = Modifier
                    .weight(slice.count.toFloat())
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(4.dp))
                    .background(slice.color)
            )
        }
    }
}

@Preview(name = "Status Stacked Bar")
@Composable
private fun StatusStackedBarPreview() {
    AppTheme(darkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.surfaceContainerHigh) {
            StatusStackedBar(
                statuses = previewStatusSlices,
                totalMembers = previewTotalMembers,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(name = "Status Stacked Bar - Dark")
@Composable
private fun StatusStackedBarDarkPreview() {
    AppTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.surfaceContainerHigh) {
            StatusStackedBar(
                statuses = previewStatusSlices,
                totalMembers = previewTotalMembers,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
