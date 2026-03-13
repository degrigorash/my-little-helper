package com.grig.myanimelist.ui.home

import com.grig.myanimelist.data.model.anime.MalAnime
import com.grig.myanimelist.data.model.manga.MalManga

enum class MalTab { Anime, Manga }

sealed interface ListState {
    data object Loading : ListState
    data object Empty : ListState
    data class AnimeContent(val animes: List<MalAnime>) : ListState
    data class MangaContent(val mangas: List<MalManga>) : ListState
    data class Error(val exception: Throwable? = null) : ListState
}
