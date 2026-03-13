package com.grig.myanimelist.data.model.anime

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MalAnimeListStatus(
    @SerialName("status")
    val status: MalAnimeWatchingStatus = MalAnimeWatchingStatus.PlanToWatch,
    @SerialName("score")
    val score: Int? = null,
    @SerialName("num_episodes_watched")
    val numEpisodesWatched: Int? = null
)
