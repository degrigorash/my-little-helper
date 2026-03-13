package com.grig.myanimelist.ui.home

import com.grig.myanimelist.data.model.MalAlternativeTitles
import com.grig.myanimelist.data.model.MalGenre
import com.grig.myanimelist.data.model.MalStudio
import com.grig.myanimelist.data.model.MalUser
import com.grig.myanimelist.data.model.anime.MalAnime
import com.grig.myanimelist.data.model.anime.MalAnimeAiringStatus
import com.grig.myanimelist.data.model.anime.MalAnimeListStatus
import com.grig.myanimelist.data.model.anime.MalAnimeWatchingStatus
import com.grig.myanimelist.data.model.manga.MalAuthor
import com.grig.myanimelist.data.model.manga.MalAuthorNode
import com.grig.myanimelist.data.model.manga.MalManga
import com.grig.myanimelist.data.model.manga.MalMangaListStatus
import com.grig.myanimelist.data.model.manga.MalMangaMediaType
import com.grig.myanimelist.data.model.manga.MalMangaPublishStatus
import com.grig.myanimelist.data.model.manga.MalMangaReadingStatus

internal val previewUser = MalUser(
    id = 1,
    name = "AnimeKing42",
    joinedAt = "2020-01-01",
    isSupporter = true
)

internal val previewAnime = MalAnime(
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

internal val previewAnimeFinished = MalAnime(
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

internal val previewAnimeCardData = AnimeCardData(
    anime = previewAnime,
    listStatus = MalAnimeListStatus(
        status = MalAnimeWatchingStatus.Watching,
        score = 9,
        numEpisodesWatched = 12
    )
)

internal val previewAnimeCardDataFinished = AnimeCardData(
    anime = previewAnimeFinished,
    listStatus = MalAnimeListStatus(
        status = MalAnimeWatchingStatus.Completed,
        score = 10,
        numEpisodesWatched = 87
    )
)

internal val previewAnimeList = listOf(previewAnime, previewAnimeFinished)

internal val previewManga = MalManga(
    id = 1,
    title = "One Piece",
    alternativeTitles = MalAlternativeTitles(),
    synopsis = "A pirate adventure.",
    mean = 9.2f,
    rank = 1,
    numListUsers = 800_000,
    mediaType = MalMangaMediaType.Manga,
    status = MalMangaPublishStatus.CurrentlyPublishing,
    numVolumes = 107,
    numChapters = 1100,
    startDate = "1997-07-22",
    authors = listOf(MalAuthorNode(MalAuthor(1870, "Eiichiro", "Oda"), "Story & Art")),
    genres = listOf(MalGenre(1, "Adventure"))
)

internal val previewMangaFinished = MalManga(
    id = 2,
    title = "Fullmetal Alchemist",
    alternativeTitles = MalAlternativeTitles(),
    synopsis = "Two brothers seek the Philosopher's Stone.",
    mean = 9.0f,
    rank = 3,
    numListUsers = 450_000,
    mediaType = MalMangaMediaType.Manga,
    status = MalMangaPublishStatus.Finished,
    numVolumes = 27,
    numChapters = 116,
    startDate = "2001-07-12",
    endDate = "2010-06-11",
    authors = listOf(MalAuthorNode(MalAuthor(1874, "Hiromu", "Arakawa"), "Story & Art")),
    genres = listOf(MalGenre(1, "Action"))
)

internal val previewMangaCardData = MangaCardData(
    manga = previewManga,
    listStatus = MalMangaListStatus(
        status = MalMangaReadingStatus.Reading,
        score = 10,
        numChaptersRead = 850,
        numVolumesRead = 95
    )
)

internal val previewMangaCardDataFinished = MangaCardData(
    manga = previewMangaFinished,
    listStatus = MalMangaListStatus(
        status = MalMangaReadingStatus.Completed,
        score = 9,
        numChaptersRead = 116,
        numVolumesRead = 27
    )
)

internal val previewMangaList = listOf(previewManga, previewMangaFinished)
