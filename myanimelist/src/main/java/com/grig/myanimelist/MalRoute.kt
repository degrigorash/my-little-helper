package com.grig.myanimelist

import kotlinx.serialization.Serializable

sealed class MalRoute {

    @Serializable
    data object MalLogin : MalRoute()

    @Serializable
    data object MalHome : MalRoute()

    @Serializable
    data object AnimeSearch : MalRoute()

    @Serializable
    data object MangaSearch : MalRoute()

    @Serializable
    data class AnimeDetail(val animeId: Int) : MalRoute()

    @Serializable
    data class MangaDetail(val mangaId: Int) : MalRoute()

    @Serializable
    data class Reviews(val mediaId: Int, val mediaType: String) : MalRoute()

    @Serializable
    data class Characters(val mediaId: Int, val mediaType: String) : MalRoute()

    @Serializable
    data class CharacterDetail(val characterId: Int) : MalRoute()
}
