package com.grig.myanimelist.ui.seasons

import com.grig.myanimelist.data.model.anime.MalAnime
import com.grig.myanimelist.data.model.anime.MalSeason

/** An airing season, e.g. "Summer 2026". Ordered chronologically. */
data class AnimeSeason(
    val year: Int,
    val season: MalSeason
) : Comparable<AnimeSeason> {

    val displayName: String get() = "${season.displayName} $year"

    /** Stable key usable in lazy-list item keys. */
    val key: String get() = "${year}_${season.name.lowercase()}"

    override fun compareTo(other: AnimeSeason): Int =
        (year * 10 + season.ordinal).compareTo(other.year * 10 + other.season.ordinal)
}

/**
 * Resolves the airing season: prefers MAL's `start_season`, otherwise derives it from
 * `start_date` when at least a month is present. Returns null when only a year or nothing is known.
 */
fun MalAnime.resolveSeason(): AnimeSeason? {
    startSeason?.let { return AnimeSeason(it.year, it.season) }
    val date = startDate ?: return null
    if (date.length < 7) return null
    val year = date.substring(0, 4).toIntOrNull() ?: return null
    val month = date.substring(5, 7).toIntOrNull() ?: return null
    val season = when (month) {
        in 1..3 -> MalSeason.Winter
        in 4..6 -> MalSeason.Spring
        in 7..9 -> MalSeason.Summer
        in 10..12 -> MalSeason.Fall
        else -> return null
    }
    return AnimeSeason(year, season)
}
