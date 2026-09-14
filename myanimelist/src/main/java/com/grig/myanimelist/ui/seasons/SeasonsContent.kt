package com.grig.myanimelist.ui.seasons

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.grig.core.theme.AppTheme
import com.grig.myanimelist.ui.animelist.AnimeCardData
import kotlinx.coroutines.launch

@Composable
fun SeasonsContent(
    groups: List<SeasonGroup>,
    onAnimeClick: (AnimeCardData) -> Unit = {},
    onAnimeLongClick: (AnimeCardData) -> Unit = {},
    watchlistIds: Set<Int> = emptySet()
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val headerIndices = remember(groups) { headerIndices(groups) }

    val selectedKey by remember(groups) {
        derivedStateOf {
            val first = listState.firstVisibleItemIndex
            val groupIndex = headerIndices.indexOfLast { it <= first }.coerceAtLeast(0)
            groups.getOrNull(groupIndex)?.key
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        SeasonStrip(
            groups = groups,
            selectedKey = selectedKey,
            onSeasonClick = { group ->
                val index = groups.indexOf(group)
                if (index >= 0) {
                    scope.launch { listState.animateScrollToItem(headerIndices[index]) }
                }
            }
        )
        SeasonsList(
            groups = groups,
            listState = listState,
            onAnimeClick = onAnimeClick,
            onAnimeLongClick = onAnimeLongClick,
            watchlistIds = watchlistIds
        )
    }
}

@Preview(name = "Seasons Content")
@Composable
private fun SeasonsContentPreview() {
    AppTheme(darkTheme = false) {
        SeasonsContent(groups = previewSeasonGroups, watchlistIds = setOf(11))
    }
}

@Preview(name = "Seasons Content - Dark")
@Composable
private fun SeasonsContentDarkPreview() {
    AppTheme(darkTheme = true) {
        SeasonsContent(groups = previewSeasonGroups)
    }
}
