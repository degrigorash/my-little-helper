package com.grig.myanimelist.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MalUser(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("picture")
    val picture: String? = null,
    @SerialName("gender")
    val gender: String? = null,
    @SerialName("birthday")
    val birthday: String? = null,
    @SerialName("location")
    val location: String? = null,
    @SerialName("joined_at")
    val joinedAt: String,
    @SerialName("anime_statistics")
    val animeStatistics: MalAnimeStatistics? = null,
    @SerialName("time_zone")
    val timeZone: String? = null,
    @SerialName("is_supporter")
    val isSupporter: Boolean = false
)
