package com.grig.myanimelist.ui.manga

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.grig.myanimelist.data.model.manga.MalManga

@Composable
fun MangaContent(
    modifier: Modifier = Modifier,
    mangas: List<MalManga>
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(mangas) { manga ->
            MangaItem(manga)
        }
    }
}