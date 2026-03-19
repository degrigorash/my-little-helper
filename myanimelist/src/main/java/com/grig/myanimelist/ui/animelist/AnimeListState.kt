package com.grig.myanimelist.ui.animelist

import com.grig.myanimelist.data.model.anime.MalAnime
import com.grig.myanimelist.data.model.anime.MalAnimeListStatus

data class AnimeCardData(
    val anime: MalAnime,
    val listStatus: MalAnimeListStatus? = null
)

sealed interface AnimeListState {
    data object Loading : AnimeListState
    data object Empty : AnimeListState
    data class Content(val animes: List<AnimeCardData>) : AnimeListState
    data class Error(val exception: Throwable? = null) : AnimeListState
}
