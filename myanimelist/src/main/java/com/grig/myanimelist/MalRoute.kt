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
}
