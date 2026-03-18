package com.grig.myanimelist.ui.mangalist

import com.grig.myanimelist.data.model.MalAlternativeTitles
import com.grig.myanimelist.data.model.MalGenre
import com.grig.myanimelist.data.model.manga.MalAuthor
import com.grig.myanimelist.data.model.manga.MalAuthorNode
import com.grig.myanimelist.data.model.manga.MalManga
import com.grig.myanimelist.data.model.manga.MalMangaListStatus
import com.grig.myanimelist.data.model.manga.MalMangaMediaType
import com.grig.myanimelist.data.model.manga.MalMangaPublishStatus
import com.grig.myanimelist.data.model.manga.MalMangaReadingStatus

val previewManga = MalManga(
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

val previewMangaFinished = MalManga(
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

val previewMangaList = listOf(previewManga, previewMangaFinished)

val previewMangaCardData = MangaCardData(
    manga = previewManga,
    listStatus = MalMangaListStatus(
        status = MalMangaReadingStatus.Reading,
        score = 10,
        numChaptersRead = 850,
        numVolumesRead = 95
    )
)

val previewMangaCardDataFinished = MangaCardData(
    manga = previewMangaFinished,
    listStatus = MalMangaListStatus(
        status = MalMangaReadingStatus.Completed,
        score = 9,
        numChaptersRead = 116,
        numVolumesRead = 27
    )
)
