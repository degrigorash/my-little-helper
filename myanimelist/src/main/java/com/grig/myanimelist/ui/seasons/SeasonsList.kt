package com.grig.myanimelist.ui.seasons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grig.core.theme.AppTheme
import com.grig.myanimelist.ui.animelist.AnimeCard
import com.grig.myanimelist.ui.animelist.AnimeCardData

@Composable
fun SeasonsList(
    groups: List<SeasonGroup>,
    listState: LazyListState = rememberLazyListState(),
    onAnimeClick: (AnimeCardData) -> Unit = {},
    onAnimeLongClick: (AnimeCardData) -> Unit = {},
    watchlistIds: Set<Int> = emptySet(),
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = listState,
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        groups.forEach { group ->
            item(key = headerKey(group)) {
                SeasonHeader(title = group.displayName)
            }
            items(group.animes, key = { it.anime.id }) { data ->
                AnimeCard(
                    data = data,
                    onClick = { onAnimeClick(data) },
                    onLongClick = { onAnimeLongClick(data) },
                    isBookmarked = data.anime.id in watchlistIds,
                    notes = data.listStatus?.comments
                )
            }
        }
    }
}

/** Lazy-list key of a group's header item. */
fun headerKey(group: SeasonGroup): String = "header_${group.key}"

/** Index of each group's header inside the flattened lazy list. */
fun headerIndices(groups: List<SeasonGroup>): List<Int> {
    var index = 0
    return groups.map { group ->
        val header = index
        index += 1 + group.animes.size
        header
    }
}

@Preview(name = "Seasons List")
@Composable
private fun SeasonsListPreview() {
    AppTheme(darkTheme = false) {
        SeasonsList(groups = previewSeasonGroups, watchlistIds = setOf(11))
    }
}

@Preview(name = "Seasons List - Dark")
@Composable
private fun SeasonsListDarkPreview() {
    AppTheme(darkTheme = true) {
        SeasonsList(groups = previewSeasonGroups)
    }
}
