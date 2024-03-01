package com.grig.myanimelist.data.model.manga

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MalMangaList(
    @SerialName("data")
    val data: List<MalMangaNode>
)

@Serializable
data class MalMangaNode(
    @SerialName("node")
    val manga: MalManga
)