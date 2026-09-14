package com.grig.myanimelist.data.model.anime

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MalStartSeason(
    @SerialName("year")
    val year: Int,
    @SerialName("season")
    val season: MalSeason = MalSeason.Winter
)

@Serializable
enum class MalSeason(val displayName: String) {
    @SerialName("winter")
    Winter("Winter"),
    @SerialName("spring")
    Spring("Spring"),
    @SerialName("summer")
    Summer("Summer"),
    @SerialName("fall")
    Fall("Fall")
}
