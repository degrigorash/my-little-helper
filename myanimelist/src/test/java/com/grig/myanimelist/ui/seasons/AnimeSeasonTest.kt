package com.grig.myanimelist.ui.seasons

import com.grig.myanimelist.data.model.anime.MalAnime
import com.grig.myanimelist.data.model.anime.MalSeason
import com.grig.myanimelist.data.model.anime.MalStartSeason
import com.grig.myanimelist.ui.animelist.AnimeCardData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AnimeSeasonTest {

    private fun anime(id: Int, startDate: String? = null, startSeason: MalStartSeason? = null, mean: Float? = null) =
        MalAnime(id = id, title = "A$id", startDate = startDate, startSeason = startSeason, mean = mean)

    @Test
    fun `prefers start_season over start_date`() {
        val a = anime(1, startDate = "2024-01-05", startSeason = MalStartSeason(2025, MalSeason.Fall))
        assertEquals(AnimeSeason(2025, MalSeason.Fall), a.resolveSeason())
    }

    @Test
    fun `derives season from start_date month`() {
        assertEquals(MalSeason.Winter, anime(1, "2026-02-01").resolveSeason()?.season)
        assertEquals(MalSeason.Spring, anime(1, "2026-04").resolveSeason()?.season)
        assertEquals(MalSeason.Summer, anime(1, "2026-09-30").resolveSeason()?.season)
        assertEquals(MalSeason.Fall, anime(1, "2026-12-31").resolveSeason()?.season)
    }

    @Test
    fun `year-only or missing date has no season`() {
        assertNull(anime(1, "2026").resolveSeason())
        assertNull(anime(1, null).resolveSeason())
    }

    @Test
    fun `groups newest first, TBA last, sorted by score with nulls last`() {
        val groups = groupBySeason(
            listOf(
                AnimeCardData(anime(1, "2025-10-01", mean = 7.0f)),
                AnimeCardData(anime(2, null)),
                AnimeCardData(anime(3, "2026-07-01", mean = 8.0f)),
                AnimeCardData(anime(4, "2025-10-15", mean = 9.0f)),
                AnimeCardData(anime(5, "2025-11-01", mean = null))
            )
        )
        assertEquals(listOf("Summer 2026", "Fall 2025", "Date TBA"), groups.map { it.displayName })
        assertEquals(listOf(4, 1, 5), groups[1].animes.map { it.anime.id })
    }
}
