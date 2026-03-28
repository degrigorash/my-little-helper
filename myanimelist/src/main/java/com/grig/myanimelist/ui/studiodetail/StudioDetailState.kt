package com.grig.myanimelist.ui.studiodetail

import com.grig.myanimelist.data.model.jikan.JikanAnimeListItem
import com.grig.myanimelist.data.model.jikan.JikanProducer

sealed interface StudioDetailState {
    data object Loading : StudioDetailState
    data class Content(
        val producer: JikanProducer,
        val animeList: List<JikanAnimeListItem> = emptyList(),
        val isLoadingAnime: Boolean = true,
        val hasMoreAnime: Boolean = false,
        val isLoadingMore: Boolean = false
    ) : StudioDetailState
    data class Error(val message: String) : StudioDetailState
}
