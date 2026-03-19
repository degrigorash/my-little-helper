package com.grig.myanimelist.ui.mangalist

import com.grig.myanimelist.data.model.manga.MalManga
import com.grig.myanimelist.data.model.manga.MalMangaListStatus

data class MangaCardData(
    val manga: MalManga,
    val listStatus: MalMangaListStatus? = null
)

sealed interface MangaListState {
    data object Loading : MangaListState
    data object Empty : MangaListState
    data class Content(val mangas: List<MangaCardData>) : MangaListState
    data class Error(val exception: Throwable? = null) : MangaListState
}
