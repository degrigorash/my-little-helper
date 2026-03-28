package com.grig.myanimelist.data.model.jikan

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JikanAnimeListResponse(
    @SerialName("pagination")
    val pagination: JikanPagination,
    @SerialName("data")
    val data: List<JikanAnimeListItem>
)

@Serializable
data class JikanAnimeListItem(
    @SerialName("mal_id")
    val malId: Int,
    @SerialName("title")
    val title: String,
    @SerialName("images")
    val images: JikanCharacterImages? = null,
    @SerialName("score")
    val score: Double? = null,
    @SerialName("type")
    val type: String? = null,
    @SerialName("year")
    val year: Int? = null,
    @SerialName("status")
    val status: String? = null
)
