package com.grig.myanimelist.ui.watchlist

import com.grig.myanimelist.data.model.anime.MalAnimeListStatus
import com.grig.myanimelist.data.model.anime.MalAnimeWatchingStatus
import com.grig.myanimelist.ui.animelist.previewAnime
import com.grig.myanimelist.ui.animelist.previewAnimeFinished

val previewWatchlistItem = WatchlistItemData(
    anime = previewAnime,
    listStatus = MalAnimeListStatus(
        status = MalAnimeWatchingStatus.PlanToWatch,
        score = 0,
        numEpisodesWatched = 0
    ),
    notes = "Recommended by Alex, watch after finishing AoT"
)

val previewWatchlistItemNoNotes = WatchlistItemData(
    anime = previewAnimeFinished,
    listStatus = MalAnimeListStatus(
        status = MalAnimeWatchingStatus.PlanToWatch
    ),
    notes = null
)
