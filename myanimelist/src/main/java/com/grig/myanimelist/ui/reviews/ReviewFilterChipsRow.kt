package com.grig.myanimelist.ui.reviews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grig.core.theme.AppTheme
import com.grig.core.theme.AppThemeExtended

@Composable
fun ReviewFilterChipsRow(
    activeFilter: ReviewFilter?,
    onToggleFilter: (ReviewFilter) -> Unit
) {
    val colors = AppThemeExtended.colorScheme

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(ReviewFilter.entries) { filter ->
            FilterChip(
                selected = filter == activeFilter,
                onClick = { onToggleFilter(filter) },
                label = { Text(filter.displayName) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = colors.malCardStart,
                    selectedLabelColor = colors.cardText
                )
            )
        }
    }
}

@Preview(name = "Review Filter Chips - None Selected")
@Composable
private fun ReviewFilterChipsRowPreview() {
    AppTheme(darkTheme = false) {
        ReviewFilterChipsRow(
            activeFilter = null,
            onToggleFilter = {}
        )
    }
}

@Preview(name = "Review Filter Chips - Dark")
@Composable
private fun ReviewFilterChipsRowDarkPreview() {
    AppTheme(darkTheme = true) {
        ReviewFilterChipsRow(
            activeFilter = ReviewFilter.RECOMMENDED,
            onToggleFilter = {}
        )
    }
}
