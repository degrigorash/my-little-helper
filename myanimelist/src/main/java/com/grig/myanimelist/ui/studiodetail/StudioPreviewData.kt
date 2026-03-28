package com.grig.myanimelist.ui.studiodetail

import com.grig.myanimelist.data.model.jikan.JikanAnimeListItem
import com.grig.myanimelist.data.model.jikan.JikanProducer
import com.grig.myanimelist.data.model.jikan.JikanProducerTitle

val previewProducer = JikanProducer(
    malId = 1,
    titles = listOf(
        JikanProducerTitle(type = "Default", title = "Studio Pierrot"),
        JikanProducerTitle(type = "Japanese", title = "スタジオぴえろ")
    ),
    favorites = 6260,
    established = "1979-05-01T00:00:00+00:00",
    about = "Pierrot Co., Ltd. is a Japanese animation studio established in May 1979 by former employees of both Tatsunoko Production and Mushi Production.",
    count = 326
)

val previewStudioAnimeList = listOf(
    JikanAnimeListItem(
        malId = 41467,
        title = "Bleach: Sennen Kessen-hen",
        score = 8.98,
        type = "TV",
        year = 2022,
        status = "Finished Airing"
    ),
    JikanAnimeListItem(
        malId = 20,
        title = "Naruto",
        score = 8.0,
        type = "TV",
        year = 2002,
        status = "Finished Airing"
    ),
    JikanAnimeListItem(
        malId = 269,
        title = "Bleach",
        score = 7.92,
        type = "TV",
        year = 2004,
        status = "Finished Airing"
    )
)
