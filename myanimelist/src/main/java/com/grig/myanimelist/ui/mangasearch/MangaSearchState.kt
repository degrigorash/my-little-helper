package com.grig.myanimelist.ui.mangasearch

import com.grig.myanimelist.data.model.jikan.ResolvedRelation
import com.grig.myanimelist.data.model.manga.MalManga

sealed interface MangaSearchState {
    val query: String
    val listChanged: Boolean

    data class Idle(
        override val query: String = "",
        override val listChanged: Boolean = false
    ) : MangaSearchState

    data class Searching(
        override val query: String,
        override val listChanged: Boolean = false
    ) : MangaSearchState

    data class Results(
        override val query: String,
        override val listChanged: Boolean = false,
        val results: List<MalManga>
    ) : MangaSearchState

    data class LoadingDetail(
        override val query: String,
        override val listChanged: Boolean = false
    ) : MangaSearchState

    data class Detail(
        override val query: String,
        override val listChanged: Boolean = false,
        val manga: MalManga,
        val isUpdatingList: Boolean = false,
        val relatedAnime: List<ResolvedRelation> = emptyList(),
        val isLoadingRelatedAnime: Boolean = false
    ) : MangaSearchState {
        val isInMyList: Boolean get() = manga.myListStatus != null
    }

    data class NoResults(
        override val query: String,
        override val listChanged: Boolean = false
    ) : MangaSearchState

    data class Error(
        override val query: String,
        override val listChanged: Boolean = false,
        val message: String
    ) : MangaSearchState
}
