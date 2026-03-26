package com.grig.myanimelist.ui.mangadetail

import com.grig.myanimelist.data.model.jikan.ResolvedRelation
import com.grig.myanimelist.data.model.manga.MalManga

sealed interface MangaDetailState {
    data object Loading : MangaDetailState
    data class Content(
        val manga: MalManga,
        val isUpdatingList: Boolean = false,
        val listChanged: Boolean = false,
        val relatedAnime: List<ResolvedRelation> = emptyList(),
        val isLoadingRelatedAnime: Boolean = false
    ) : MangaDetailState {
        val isInMyList: Boolean get() = manga.myListStatus != null
    }
    data class Error(val message: String) : MangaDetailState
}
