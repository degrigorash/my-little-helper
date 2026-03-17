package com.grig.myanimelist.ui.mangasearch

import com.grig.myanimelist.data.model.manga.MalManga

data class MangaSearchState(
    val query: String = "",
    val searchResults: List<MalManga> = emptyList(),
    val isSearching: Boolean = false,
    val selectedManga: MalManga? = null,
    val isLoadingDetail: Boolean = false,
    val isUpdatingList: Boolean = false,
    val error: String? = null
) {
    val isInMyList: Boolean
        get() = selectedManga?.myListStatus != null
}
