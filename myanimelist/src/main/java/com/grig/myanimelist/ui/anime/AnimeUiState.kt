package com.grig.myanimelist.ui.anime

import com.grig.myanimelist.data.model.anime.MalAnime

sealed interface AnimeUiState {
    data object Loading : AnimeUiState
    data object Empty : AnimeUiState
    data class Content(val animes: List<MalAnime>) : AnimeUiState
    data class Error(val exception: Throwable? = null) : AnimeUiState
}