package com.grig.myanimelist.data.model.jikan

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JikanPersonFullResponse(
    @SerialName("data")
    val data: JikanPersonFull
)

@Serializable
data class JikanPersonFull(
    @SerialName("mal_id")
    val malId: Int,
    @SerialName("name")
    val name: String,
    @SerialName("given_name")
    val givenName: String? = null,
    @SerialName("family_name")
    val familyName: String? = null,
    @SerialName("images")
    val images: JikanPersonImages? = null,
    @SerialName("birthday")
    val birthday: String? = null,
    @SerialName("favorites")
    val favorites: Int = 0,
    @SerialName("about")
    val about: String? = null,
    @SerialName("anime")
    val anime: List<JikanPersonAnimeEntry> = emptyList(),
    @SerialName("manga")
    val manga: List<JikanPersonMangaEntry> = emptyList()
)

@Serializable
data class JikanPersonAnimeEntry(
    @SerialName("position")
    val position: String,
    @SerialName("anime")
    val anime: JikanAnimeMeta
)

@Serializable
data class JikanPersonMangaEntry(
    @SerialName("position")
    val position: String,
    @SerialName("manga")
    val manga: JikanMangaMeta
)
