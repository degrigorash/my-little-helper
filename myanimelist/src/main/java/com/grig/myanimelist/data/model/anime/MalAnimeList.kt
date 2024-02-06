package com.grig.myanimelist.data.model.anime

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MalAnimeList(
    @SerialName("data")
    val data: List<MalAnimeNode>
)

@Serializable
data class MalAnimeNode(
    @SerialName("node")
    val anime: MalAnime
)