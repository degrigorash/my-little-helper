package com.grig.myanimelist.ui.animesearch

import com.grig.myanimelist.data.model.anime.MalAnime
import com.grig.myanimelist.data.model.jikan.ResolvedRelation

data class AnimeSearchState(
    val query: String = "",
    val searchResults: List<MalAnime> = emptyList(),
    val isSearching: Boolean = false,
    val selectedAnime: MalAnime? = null,
    val isLoadingDetail: Boolean = false,
    val isUpdatingList: Boolean = false,
    val listChanged: Boolean = false,
    val error: String? = null,
    val relatedManga: List<ResolvedRelation> = emptyList(),
    val isLoadingRelatedManga: Boolean = false
) {
    val isInMyList: Boolean
        get() = selectedAnime?.myListStatus != null
}
