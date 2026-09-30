package com.grig.myanimelist.data.model.jikan

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JikanStatisticsResponse(
    @SerialName("data")
    val data: JikanStatistics
)

/**
 * Shared shape of `/anime/{id}/statistics` and `/manga/{id}/statistics`. The two only
 * differ in the in-progress and planned buckets: anime reports `watching` /
 * `plan_to_watch`, manga reports `reading` / `plan_to_read`. Both pairs are modeled
 * and left null when absent.
 */
@Serializable
data class JikanStatistics(
    @SerialName("watching")
    val watching: Int? = null,
    @SerialName("reading")
    val reading: Int? = null,
    @SerialName("completed")
    val completed: Int = 0,
    @SerialName("on_hold")
    val onHold: Int = 0,
    @SerialName("dropped")
    val dropped: Int = 0,
    @SerialName("plan_to_watch")
    val planToWatch: Int? = null,
    @SerialName("plan_to_read")
    val planToRead: Int? = null,
    @SerialName("total")
    val total: Int = 0,
    @SerialName("scores")
    val scores: List<JikanScoreBucket> = emptyList()
)

/** One bar of the 1–10 score histogram; [percentage] is the share of all votes (0–100). */
@Serializable
data class JikanScoreBucket(
    @SerialName("score")
    val score: Int,
    @SerialName("votes")
    val votes: Int = 0,
    @SerialName("percentage")
    val percentage: Float = 0f
)
