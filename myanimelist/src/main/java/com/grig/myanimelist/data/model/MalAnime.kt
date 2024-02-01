package com.grig.myanimelist.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MalAnime(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("alternative_titles")
    val alternativeTitles: MalAlternativeTitles,
    @SerialName("synopsis")
    val synopsis: String,
    @SerialName("mean")
    val mean: Float? = null,
    @SerialName("rank")
    val rank: Int? = null,
    @SerialName("popularity")
    val popularity: Int? = null,
    @SerialName("status")
    val status: MalStatus = MalStatus.NotYetAired,
    @SerialName("num_episodes")
    val numEpisodes: Int? = null,
    @SerialName("studios")
    val studios: List<MalStudio> = emptyList(),
    @SerialName("main_picture")
    val pictures: MalPictures? = null
)

@Serializable
data class MalAlternativeTitles(
    @SerialName("synonyms")
    val synonyms: List<String> = emptyList(),
    @SerialName("en")
    val en: String? = null,
    @SerialName("ja")
    val ja: String? = null
)