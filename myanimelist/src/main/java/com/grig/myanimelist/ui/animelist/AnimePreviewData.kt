package com.grig.myanimelist.ui.animelist

import com.grig.myanimelist.data.model.MalAlternativeTitles
import com.grig.myanimelist.data.model.MalGenre
import com.grig.myanimelist.data.model.MalStudio
import com.grig.myanimelist.data.model.anime.MalAnime
import com.grig.myanimelist.data.model.anime.MalAnimeAiringStatus
import com.grig.myanimelist.data.model.anime.MalAnimeListStatus
import com.grig.myanimelist.data.model.anime.MalAnimeWatchingStatus

val previewAnime = MalAnime(
    id = 1,
    title = "Demon Slayer: Kimetsu no Yaiba - Infinity Castle",
    alternativeTitles = MalAlternativeTitles(),
    synopsis = "The continuation of Demon Slayer.",
    mean = 8.9f,
    rank = 1,
    numListUsers = 2_100_000,
    status = MalAnimeAiringStatus.CurrentlyAiring,
    numEpisodes = 26,
    startDate = "2025-10-03",
    genres = listOf(MalGenre(1, "Action")),
    studios = listOf(MalStudio(43, "ufotable"))
)

val previewAnimeFinished = MalAnime(
    id = 2,
    title = "Attack on Titan: The Final Season",
    alternativeTitles = MalAlternativeTitles(),
    synopsis = "The final season of Attack on Titan.",
    mean = 9.1f,
    rank = 2,
    numListUsers = 3_500_000,
    status = MalAnimeAiringStatus.FinishedAiring,
    numEpisodes = 87,
    startDate = "2020-12-07",
    endDate = "2023-11-05",
    genres = listOf(MalGenre(1, "Action")),
    studios = listOf(MalStudio(569, "MAPPA"))
)

val previewAnimeList = listOf(previewAnime, previewAnimeFinished)

val previewAnimeCardData = AnimeCardData(
    anime = previewAnime,
    listStatus = MalAnimeListStatus(
        status = MalAnimeWatchingStatus.Watching,
        score = 9,
        numEpisodesWatched = 12
    )
)

val previewAnimeCardDataFinished = AnimeCardData(
    anime = previewAnimeFinished,
    listStatus = MalAnimeListStatus(
        status = MalAnimeWatchingStatus.Completed,
        score = 10,
        numEpisodesWatched = 87
    )
)
