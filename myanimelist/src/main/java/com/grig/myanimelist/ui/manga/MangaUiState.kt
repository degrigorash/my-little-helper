package com.grig.myanimelist.ui.manga

import com.grig.myanimelist.data.model.manga.MalManga

sealed interface MangaUiState {
    data object Loading : MangaUiState
    data object Empty : MangaUiState
    data class Content(val mangas: List<MalManga>) : MangaUiState
    data class Error(val exception: Throwable? = null) : MangaUiState
}