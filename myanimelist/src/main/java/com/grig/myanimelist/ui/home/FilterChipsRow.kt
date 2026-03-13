package com.grig.myanimelist.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grig.core.theme.AppTheme
import com.grig.myanimelist.data.model.anime.MalAnimeWatchingStatus
import com.grig.myanimelist.data.model.manga.MalMangaReadingStatus

@Composable
fun FilterChipsRow(
    activeTab: MalTab,
    animeFilter: MalAnimeWatchingStatus,
    mangaFilter: MalMangaReadingStatus,
    onAnimeFilterSelected: (MalAnimeWatchingStatus) -> Unit,
    onMangaFilterSelected: (MalMangaReadingStatus) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        when (activeTab) {
            MalTab.Anime -> items(MalAnimeWatchingStatus.entries) { status ->
                FilterChip(
                    selected = status == animeFilter,
                    onClick = { onAnimeFilterSelected(status) },
                    label = { Text(status.displayName) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }

            MalTab.Manga -> items(MalMangaReadingStatus.entries) { status ->
                FilterChip(
                    selected = status == mangaFilter,
                    onClick = { onMangaFilterSelected(status) },
                    label = { Text(status.displayName) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }
    }
}

@Preview(name = "Anime Filters")
@Composable
private fun FilterChipsRowAnimePreview() {
    AppTheme(darkTheme = false) {
        FilterChipsRow(
            activeTab = MalTab.Anime,
            animeFilter = MalAnimeWatchingStatus.PlanToWatch,
            mangaFilter = MalMangaReadingStatus.PlanToRead,
            onAnimeFilterSelected = {},
            onMangaFilterSelected = {}
        )
    }
}

@Preview(name = "Manga Filters")
@Composable
private fun FilterChipsRowMangaPreview() {
    AppTheme(darkTheme = false) {
        FilterChipsRow(
            activeTab = MalTab.Manga,
            animeFilter = MalAnimeWatchingStatus.PlanToWatch,
            mangaFilter = MalMangaReadingStatus.Reading,
            onAnimeFilterSelected = {},
            onMangaFilterSelected = {}
        )
    }
}
