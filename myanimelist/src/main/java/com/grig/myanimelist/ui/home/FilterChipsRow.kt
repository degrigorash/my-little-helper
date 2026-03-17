package com.grig.myanimelist.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grig.core.theme.AppTheme
import com.grig.core.theme.AppThemeExtended
import com.grig.myanimelist.R
import com.grig.myanimelist.data.model.anime.MalAnimeWatchingStatus
import com.grig.myanimelist.data.model.manga.MalMangaReadingStatus

@Composable
fun FilterChipsRow(
    activeTab: MalTab,
    animeFilter: Set<MalAnimeWatchingStatus>,
    mangaFilter: Set<MalMangaReadingStatus>,
    upcomingFilter: Boolean,
    onAnimeFilterSelected: (MalAnimeWatchingStatus) -> Unit,
    onMangaFilterSelected: (MalMangaReadingStatus) -> Unit,
    onUpcomingFilterToggle: () -> Unit
) {
    val colors = AppThemeExtended.colorScheme

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        item {
            UpcomingFilterButton(
                selected = upcomingFilter,
                onClick = onUpcomingFilterToggle
            )
        }

        when (activeTab) {
            MalTab.Anime -> items(MalAnimeWatchingStatus.entries) { status ->
                FilterChip(
                    selected = status in animeFilter,
                    onClick = { onAnimeFilterSelected(status) },
                    label = { Text(status.displayName) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = colors.malCardStart,
                        selectedLabelColor = colors.cardText
                    )
                )
            }

            MalTab.Manga -> items(MalMangaReadingStatus.entries) { status ->
                FilterChip(
                    selected = status in mangaFilter,
                    onClick = { onMangaFilterSelected(status) },
                    label = { Text(status.displayName) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = colors.malCardStart,
                        selectedLabelColor = colors.cardText
                    )
                )
            }
        }
    }
}

@Composable
private fun UpcomingFilterButton(
    selected: Boolean,
    onClick: () -> Unit
) {
    val colors = AppThemeExtended.colorScheme

    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .then(
                if (selected) {
                    Modifier.background(colors.malCardStart)
                } else {
                    Modifier.border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                }
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_upcoming),
            contentDescription = "Upcoming",
            modifier = Modifier.size(18.dp),
            tint = if (selected) colors.cardText else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(name = "Anime Filters")
@Composable
private fun FilterChipsRowAnimePreview() {
    AppTheme(darkTheme = false) {
        FilterChipsRow(
            activeTab = MalTab.Anime,
            animeFilter = setOf(MalAnimeWatchingStatus.PlanToWatch),
            mangaFilter = emptySet(),
            upcomingFilter = false,
            onAnimeFilterSelected = {},
            onMangaFilterSelected = {},
            onUpcomingFilterToggle = {}
        )
    }
}

@Preview(name = "Manga Filters")
@Composable
private fun FilterChipsRowMangaPreview() {
    AppTheme(darkTheme = false) {
        FilterChipsRow(
            activeTab = MalTab.Manga,
            animeFilter = emptySet(),
            mangaFilter = setOf(MalMangaReadingStatus.Reading),
            upcomingFilter = true,
            onAnimeFilterSelected = {},
            onMangaFilterSelected = {},
            onUpcomingFilterToggle = {}
        )
    }
}
