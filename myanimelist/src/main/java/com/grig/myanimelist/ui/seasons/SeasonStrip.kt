package com.grig.myanimelist.ui.seasons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grig.core.theme.AppTheme
import com.grig.core.theme.AppThemeExtended

@Composable
fun SeasonStrip(
    groups: List<SeasonGroup>,
    selectedKey: String?,
    onSeasonClick: (SeasonGroup) -> Unit
) {
    val colors = AppThemeExtended.colorScheme
    val rowState = rememberLazyListState()

    LaunchedEffect(selectedKey) {
        val index = groups.indexOfFirst { it.key == selectedKey }
        if (index >= 0) rowState.animateScrollToItem(index)
    }

    LazyRow(
        state = rowState,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(groups, key = { it.key }) { group ->
            FilterChip(
                selected = group.key == selectedKey,
                onClick = { onSeasonClick(group) },
                label = { Text(group.displayName) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = colors.malCardStart,
                    selectedLabelColor = colors.cardText
                )
            )
        }
    }
}

@Preview(name = "Season Strip")
@Composable
private fun SeasonStripPreview() {
    AppTheme(darkTheme = false) {
        SeasonStrip(
            groups = previewSeasonGroups,
            selectedKey = previewSeasonGroups.first().key,
            onSeasonClick = {}
        )
    }
}

@Preview(name = "Season Strip - Dark")
@Composable
private fun SeasonStripDarkPreview() {
    AppTheme(darkTheme = true) {
        SeasonStrip(
            groups = previewSeasonGroups,
            selectedKey = previewSeasonGroups[1].key,
            onSeasonClick = {}
        )
    }
}
