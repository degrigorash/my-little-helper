package com.grig.myanimelist.ui.seasons

import com.grig.myanimelist.data.model.anime.MalAnimeAiringStatus
import com.grig.myanimelist.data.model.anime.MalAnimeListStatus
import com.grig.myanimelist.data.model.anime.MalAnimeWatchingStatus
import com.grig.myanimelist.data.model.anime.MalSeason
import com.grig.myanimelist.data.model.anime.MalStartSeason
import com.grig.myanimelist.ui.animelist.AnimeCardData
import com.grig.myanimelist.ui.animelist.previewAnime
import com.grig.myanimelist.ui.animelist.previewAnimeFinished

private val planToWatch = MalAnimeListStatus(
    status = MalAnimeWatchingStatus.PlanToWatch,
    score = 0,
    numEpisodesWatched = 0
)

val previewSeasonSummer2026 = AnimeSeason(2026, MalSeason.Summer)
val previewSeasonFall2025 = AnimeSeason(2025, MalSeason.Fall)

val previewSeasonGroups = listOf(
    SeasonGroup(
        season = previewSeasonSummer2026,
        animes = listOf(
            AnimeCardData(
                anime = previewAnime.copy(
                    id = 11,
                    title = "Frieren: Beyond Journey's End Season 2",
                    startDate = "2026-07-04",
                    startSeason = MalStartSeason(2026, MalSeason.Summer)
                ),
                listStatus = planToWatch.copy(comments = "Everyone says S1 was a masterpiece.")
            )
        )
    ),
    SeasonGroup(
        season = previewSeasonFall2025,
        animes = listOf(
            AnimeCardData(
                anime = previewAnime.copy(startSeason = MalStartSeason(2025, MalSeason.Fall)),
                listStatus = planToWatch
            ),
            AnimeCardData(
                anime = previewAnimeFinished.copy(
                    id = 12,
                    mean = 7.4f,
                    startDate = "2025-10-10",
                    startSeason = MalStartSeason(2025, MalSeason.Fall)
                ),
                listStatus = planToWatch
            )
        )
    ),
    SeasonGroup(
        season = null,
        animes = listOf(
            AnimeCardData(
                anime = previewAnime.copy(
                    id = 13,
                    title = "Chainsaw Man Part 2",
                    mean = null,
                    startDate = null,
                    status = MalAnimeAiringStatus.NotYetAired
                ),
                listStatus = planToWatch
            )
        )
    )
)
