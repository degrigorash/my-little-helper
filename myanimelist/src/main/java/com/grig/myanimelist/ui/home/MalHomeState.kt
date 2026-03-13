package com.grig.myanimelist.ui.home

import com.grig.myanimelist.data.model.anime.MalAnime
import com.grig.myanimelist.data.model.anime.MalAnimeListStatus
import com.grig.myanimelist.data.model.manga.MalManga
import com.grig.myanimelist.data.model.manga.MalMangaListStatus

enum class MalTab { Anime, Manga }

data class AnimeCardData(
    val anime: MalAnime,
    val listStatus: MalAnimeListStatus? = null
)

data class MangaCardData(
    val manga: MalManga,
    val listStatus: MalMangaListStatus? = null
)

sealed interface ListState {
    data object Loading : ListState
    data object Empty : ListState
    data class AnimeContent(val animes: List<AnimeCardData>) : ListState
    data class MangaContent(val mangas: List<MangaCardData>) : ListState
    data class Error(val exception: Throwable? = null) : ListState
}
