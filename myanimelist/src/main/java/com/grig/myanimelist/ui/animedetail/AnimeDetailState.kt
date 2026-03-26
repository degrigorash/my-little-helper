package com.grig.myanimelist.ui.animedetail

import com.grig.myanimelist.data.model.anime.MalAnime
import com.grig.myanimelist.data.model.jikan.ResolvedRelation

sealed interface AnimeDetailState {
    data object Loading : AnimeDetailState
    data class Content(
        val anime: MalAnime,
        val isUpdatingList: Boolean = false,
        val listChanged: Boolean = false,
        val relatedManga: List<ResolvedRelation> = emptyList(),
        val isLoadingRelatedManga: Boolean = false
    ) : AnimeDetailState {
        val isInMyList: Boolean get() = anime.myListStatus != null
    }
    data class Error(val message: String) : AnimeDetailState
}
