package com.grig.myanimelist.ui.scorestats

import androidx.compose.ui.graphics.Color
import com.grig.myanimelist.data.model.jikan.JikanScoreBucket

enum class ScoreStatsMediaType(val value: String) {
    ANIME("anime"),
    MANGA("manga");

    companion object {
        fun fromValue(value: String) = entries.first { it.value == value }
    }
}

/** One list-status bucket (Watching, Completed, …) with its member count. */
data class StatusSlice(
    val label: String,
    val count: Int,
    val color: Color
)

sealed interface ScoreStatsState {
    data object Loading : ScoreStatsState
    data object Empty : ScoreStatsState
    data class Content(
        /** Always ten buckets, ordered 10 → 1. */
        val scores: List<JikanScoreBucket>,
        val totalVotes: Int,
        val statuses: List<StatusSlice>,
        val totalMembers: Int
    ) : ScoreStatsState
    data class Error(val message: String) : ScoreStatsState
}
