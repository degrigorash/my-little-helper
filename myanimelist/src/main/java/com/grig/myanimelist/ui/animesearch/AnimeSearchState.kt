package com.grig.myanimelist.ui.animesearch

import com.grig.myanimelist.data.model.anime.MalAnime

sealed interface AnimeSearchState {
    val query: String

    data class Idle(
        override val query: String = ""
    ) : AnimeSearchState

    data class Searching(
        override val query: String
    ) : AnimeSearchState

    data class Results(
        override val query: String,
        val results: List<MalAnime>
    ) : AnimeSearchState

    data class NoResults(
        override val query: String
    ) : AnimeSearchState

    data class Error(
        override val query: String,
        val message: String
    ) : AnimeSearchState
}
