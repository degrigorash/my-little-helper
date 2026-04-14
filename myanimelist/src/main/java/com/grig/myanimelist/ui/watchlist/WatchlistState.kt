package com.grig.myanimelist.ui.watchlist

import com.grig.myanimelist.data.model.anime.MalAnime
import com.grig.myanimelist.data.model.anime.MalAnimeListStatus

data class WatchlistItemData(
    val anime: MalAnime,
    val listStatus: MalAnimeListStatus? = null,
    val notes: String? = null
)

sealed interface WatchlistState {
    data object Loading : WatchlistState
    data object Empty : WatchlistState
    data class Content(val items: List<WatchlistItemData>) : WatchlistState
    data class Error(val exception: Throwable? = null) : WatchlistState
}
