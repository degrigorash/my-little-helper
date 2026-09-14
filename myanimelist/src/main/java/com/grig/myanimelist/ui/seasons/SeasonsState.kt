package com.grig.myanimelist.ui.seasons

import com.grig.myanimelist.ui.animelist.AnimeCardData

/** One season section. [season] is null for entries whose airing date is not known yet. */
data class SeasonGroup(
    val season: AnimeSeason?,
    val animes: List<AnimeCardData>
) {
    val key: String get() = season?.key ?: TBA_KEY
    val displayName: String get() = season?.displayName ?: TBA_DISPLAY_NAME

    companion object {
        const val TBA_KEY = "tba"
        const val TBA_DISPLAY_NAME = "Date TBA"
    }
}

sealed interface SeasonsState {
    data object Loading : SeasonsState
    data object Empty : SeasonsState
    data class Content(val groups: List<SeasonGroup>) : SeasonsState
    data class Error(val exception: Throwable? = null) : SeasonsState
}
