package com.grig.myanimelist.ui.characterdetail

import com.grig.myanimelist.data.model.jikan.JikanAnimeMeta
import com.grig.myanimelist.data.model.jikan.JikanCharacterAnimeEntry
import com.grig.myanimelist.data.model.jikan.JikanCharacterFull
import com.grig.myanimelist.data.model.jikan.JikanCharacterMangaEntry
import com.grig.myanimelist.data.model.jikan.JikanCharacterVoiceEntry
import com.grig.myanimelist.data.model.jikan.JikanMangaMeta
import com.grig.myanimelist.data.model.jikan.JikanPersonMeta

val previewCharacterFull = JikanCharacterFull(
    malId = 40,
    url = "https://myanimelist.net/character/40/Luffy",
    name = "Monkey D. Luffy",
    nameKanji = "モンキー・D・ルフィ",
    nicknames = listOf("Straw Hat", "Mugiwara"),
    favorites = 120543,
    about = "Monkey D. Luffy is the protagonist of the manga and anime One Piece. He is the founder and captain of the increasingly infamous and powerful Straw Hat Pirates. His lifelong dream is to become the Pirate King by finding the legendary treasure left behind by the late Gol D. Roger.",
    anime = listOf(
        JikanCharacterAnimeEntry(
            role = "Main",
            anime = JikanAnimeMeta(
                malId = 21,
                url = "https://myanimelist.net/anime/21",
                title = "One Piece"
            )
        ),
        JikanCharacterAnimeEntry(
            role = "Main",
            anime = JikanAnimeMeta(
                malId = 2022,
                url = "https://myanimelist.net/anime/2022",
                title = "One Piece Film: Strong World"
            )
        )
    ),
    manga = listOf(
        JikanCharacterMangaEntry(
            role = "Main",
            manga = JikanMangaMeta(
                malId = 13,
                url = "https://myanimelist.net/manga/13",
                title = "One Piece"
            )
        )
    ),
    voices = listOf(
        JikanCharacterVoiceEntry(
            language = "Japanese",
            person = JikanPersonMeta(
                malId = 40,
                url = "https://myanimelist.net/people/40",
                name = "Tanaka, Mayumi"
            )
        ),
        JikanCharacterVoiceEntry(
            language = "English",
            person = JikanPersonMeta(
                malId = 428,
                url = "https://myanimelist.net/people/428",
                name = "Clinkenbeard, Colleen"
            )
        )
    )
)

val previewCharacterFullMinimal = JikanCharacterFull(
    malId = 999,
    url = "https://myanimelist.net/character/999",
    name = "Minor Character",
    favorites = 0,
    about = null
)
