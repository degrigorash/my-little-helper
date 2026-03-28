package com.grig.myanimelist.data.model.jikan

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JikanCharacterFullResponse(
    @SerialName("data")
    val data: JikanCharacterFull
)

@Serializable
data class JikanCharacterFull(
    @SerialName("mal_id")
    val malId: Int,
    @SerialName("url")
    val url: String,
    @SerialName("images")
    val images: JikanCharacterImages? = null,
    @SerialName("name")
    val name: String,
    @SerialName("name_kanji")
    val nameKanji: String? = null,
    @SerialName("nicknames")
    val nicknames: List<String> = emptyList(),
    @SerialName("favorites")
    val favorites: Int = 0,
    @SerialName("about")
    val about: String? = null,
    @SerialName("anime")
    val anime: List<JikanCharacterAnimeEntry> = emptyList(),
    @SerialName("manga")
    val manga: List<JikanCharacterMangaEntry> = emptyList(),
    @SerialName("voices")
    val voices: List<JikanCharacterVoiceEntry> = emptyList()
)

@Serializable
data class JikanCharacterAnimeEntry(
    @SerialName("role")
    val role: String,
    @SerialName("anime")
    val anime: JikanAnimeMeta
)

@Serializable
data class JikanCharacterMangaEntry(
    @SerialName("role")
    val role: String,
    @SerialName("manga")
    val manga: JikanMangaMeta
)

@Serializable
data class JikanCharacterVoiceEntry(
    @SerialName("language")
    val language: String,
    @SerialName("person")
    val person: JikanPersonMeta
)

@Serializable
data class JikanAnimeMeta(
    @SerialName("mal_id")
    val malId: Int,
    @SerialName("url")
    val url: String,
    @SerialName("images")
    val images: JikanCharacterImages? = null,
    @SerialName("title")
    val title: String
)

@Serializable
data class JikanMangaMeta(
    @SerialName("mal_id")
    val malId: Int,
    @SerialName("url")
    val url: String,
    @SerialName("images")
    val images: JikanCharacterImages? = null,
    @SerialName("title")
    val title: String
)
