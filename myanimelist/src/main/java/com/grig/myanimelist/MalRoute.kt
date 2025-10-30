package com.grig.myanimelist

import kotlinx.serialization.Serializable

sealed class MalRoute {

    @Serializable
    data object MalHome : MalRoute()

    @Serializable
    data class MalAnimeList(val username: String?) : MalRoute()

    @Serializable
    data class MalMangaList(val username: String?) : MalRoute()
}