package com.grig.myanimelist.ui.home

import com.grig.myanimelist.data.model.MalAlternativeTitles
import com.grig.myanimelist.data.model.MalGenre
import com.grig.myanimelist.data.model.MalUser
import com.grig.myanimelist.data.model.anime.MalAnime
import com.grig.myanimelist.data.model.anime.MalAnimeAiringStatus
import com.grig.myanimelist.data.model.manga.MalManga
import com.grig.myanimelist.data.model.manga.MalMangaMediaType
import com.grig.myanimelist.data.model.manga.MalMangaPublishStatus

internal val previewUser = MalUser(
    id = 1,
    name = "AnimeKing42",
    joinedAt = "2020-01-01",
    isSupporter = true
)

internal val previewAnime = MalAnime(
    id = 1,
    title = "Demon Slayer: Infinity Castle",
    alternativeTitles = MalAlternativeTitles(),
    synopsis = "The continuation of Demon Slayer.",
    mean = 8.9f,
    rank = 1,
    numListUsers = 2_100_000,
    status = MalAnimeAiringStatus.CurrentlyAiring,
    numEpisodes = 26,
    genres = listOf(MalGenre(1, "Action"))
)

internal val previewAnimeFinished = MalAnime(
    id = 2,
    title = "Attack on Titan: Final",
    alternativeTitles = MalAlternativeTitles(),
    synopsis = "The final season of Attack on Titan.",
    mean = 9.1f,
    rank = 2,
    numListUsers = 3_500_000,
    status = MalAnimeAiringStatus.FinishedAiring,
    numEpisodes = 87,
    genres = listOf(MalGenre(1, "Action"))
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
    genres = listOf(MalGenre(1, "Action"))
)

internal val previewMangaList = listOf(previewManga, previewMangaFinished)
