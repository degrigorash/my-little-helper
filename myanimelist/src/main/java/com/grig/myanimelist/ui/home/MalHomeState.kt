package com.grig.myanimelist.ui.home

import com.grig.myanimelist.data.model.anime.MalAnime
import com.grig.myanimelist.data.model.anime.MalAnimeListStatus
import com.grig.myanimelist.data.model.manga.MalManga

enum class MalTab { Anime, Manga }

data class AnimeCardData(
    val anime: MalAnime,
    val listStatus: MalAnimeListStatus? = null
)

sealed interface ListState {
    data object Loading : ListState
    data object Empty : ListState
    data class AnimeContent(val animes: List<AnimeCardData>) : ListState
    data class MangaContent(val mangas: List<MalManga>) : ListState
    data class Error(val exception: Throwable? = null) : ListState
}
