package com.grig.myanimelist.ui.animesearch

import com.grig.myanimelist.data.model.anime.MalAnime
import com.grig.myanimelist.data.model.jikan.ResolvedRelation

sealed interface AnimeSearchState {
    val query: String
    val listChanged: Boolean

    data class Idle(
        override val query: String = "",
        override val listChanged: Boolean = false
    ) : AnimeSearchState

    data class Searching(
        override val query: String,
        override val listChanged: Boolean = false
    ) : AnimeSearchState

    data class Results(
        override val query: String,
        override val listChanged: Boolean = false,
        val results: List<MalAnime>
    ) : AnimeSearchState

    data class LoadingDetail(
        override val query: String,
        override val listChanged: Boolean = false
    ) : AnimeSearchState

    data class Detail(
        override val query: String,
        override val listChanged: Boolean = false,
        val anime: MalAnime,
        val isUpdatingList: Boolean = false,
        val relatedManga: List<ResolvedRelation> = emptyList(),
        val isLoadingRelatedManga: Boolean = false
    ) : AnimeSearchState {
        val isInMyList: Boolean get() = anime.myListStatus != null
    }

    data class NoResults(
        override val query: String,
        override val listChanged: Boolean = false
    ) : AnimeSearchState

    data class Error(
        override val query: String,
        override val listChanged: Boolean = false,
        val message: String
    ) : AnimeSearchState
}
