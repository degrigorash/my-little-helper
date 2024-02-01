package com.grig.myanimelist.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MalAnimeList(
    @SerialName("data")
    val data: List<MalNode>
)

@Serializable
data class MalNode(
    @SerialName("node")
    val anime: MalAnime
)