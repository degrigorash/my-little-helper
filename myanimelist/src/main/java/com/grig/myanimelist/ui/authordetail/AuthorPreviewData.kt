package com.grig.myanimelist.ui.authordetail

import com.grig.myanimelist.data.model.jikan.JikanAnimeMeta
import com.grig.myanimelist.data.model.jikan.JikanMangaMeta
import com.grig.myanimelist.data.model.jikan.JikanPersonAnimeEntry
import com.grig.myanimelist.data.model.jikan.JikanPersonFull
import com.grig.myanimelist.data.model.jikan.JikanPersonMangaEntry

val previewPersonFull = JikanPersonFull(
    malId = 1881,
    name = "Eiichiro Oda",
    givenName = "栄一郎",
    familyName = "尾田",
    birthday = "1975-01-01T00:00:00+00:00",
    favorites = 50000,
    about = "Eiichiro Oda is a Japanese manga artist best known for his manga series One Piece, which has been serialized in Shueisha's Weekly Shonen Jump since 1997.",
    manga = listOf(
        JikanPersonMangaEntry(
            position = "Story & Art",
            manga = JikanMangaMeta(
                malId = 13,
                url = "https://myanimelist.net/manga/13",
                title = "One Piece"
            )
        ),
        JikanPersonMangaEntry(
            position = "Story & Art",
            manga = JikanMangaMeta(
                malId = 44347,
                url = "https://myanimelist.net/manga/44347",
                title = "One Piece Party"
            )
        )
    ),
    anime = listOf(
        JikanPersonAnimeEntry(
            position = "add Original Creator",
            anime = JikanAnimeMeta(
                malId = 21,
                url = "https://myanimelist.net/anime/21",
                title = "One Piece"
            )
        )
    )
)
