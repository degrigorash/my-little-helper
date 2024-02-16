package com.grig.myanimelist.ui.anime

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.grig.myanimelist.data.model.anime.MalAnime

@Composable
fun AnimeContent(
    modifier: Modifier = Modifier,
    animes: List<MalAnime>
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(animes) { anime ->
            AnimeItem(anime)
        }
    }
}