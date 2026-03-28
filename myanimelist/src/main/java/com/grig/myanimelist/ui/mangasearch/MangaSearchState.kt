package com.grig.myanimelist.ui.mangasearch

import com.grig.myanimelist.data.model.manga.MalManga

sealed interface MangaSearchState {
    val query: String

    data class Idle(
        override val query: String = ""
    ) : MangaSearchState

    data class Searching(
        override val query: String
    ) : MangaSearchState

    data class Results(
        override val query: String,
        val results: List<MalManga>
    ) : MangaSearchState

    data class NoResults(
        override val query: String
    ) : MangaSearchState

    data class Error(
        override val query: String,
        val message: String
    ) : MangaSearchState
}
