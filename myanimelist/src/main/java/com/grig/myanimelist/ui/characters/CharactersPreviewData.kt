package com.grig.myanimelist.ui.characters

import com.grig.myanimelist.data.model.jikan.JikanCharacterEntry
import com.grig.myanimelist.data.model.jikan.JikanCharacterMeta
import com.grig.myanimelist.data.model.jikan.JikanPersonMeta
import com.grig.myanimelist.data.model.jikan.JikanVoiceActor

val previewCharacterEntry = JikanCharacterEntry(
    character = JikanCharacterMeta(
        malId = 40,
        url = "https://myanimelist.net/character/40/Luffy",
        name = "Monkey D. Luffy"
    ),
    role = "Main",
    voiceActors = listOf(
        JikanVoiceActor(
            person = JikanPersonMeta(
                malId = 40,
                url = "https://myanimelist.net/people/40",
                name = "Tanaka, Mayumi"
            ),
            language = "Japanese"
        )
    )
)

val previewCharacterEntrySupporting = JikanCharacterEntry(
    character = JikanCharacterMeta(
        malId = 62,
        url = "https://myanimelist.net/character/62/Shanks",
        name = "Shanks"
    ),
    role = "Supporting",
    voiceActors = listOf(
        JikanVoiceActor(
            person = JikanPersonMeta(
                malId = 62,
                url = "https://myanimelist.net/people/62",
                name = "Ikeda, Shuuichi"
            ),
            language = "Japanese"
        )
    )
)

val previewCharacterEntryNoVa = JikanCharacterEntry(
    character = JikanCharacterMeta(
        malId = 100,
        url = "https://myanimelist.net/character/100",
        name = "Roronoa Zoro"
    ),
    role = "Main",
    voiceActors = emptyList()
)

val previewCharacterEntries = listOf(
    previewCharacterEntry,
    previewCharacterEntrySupporting,
    previewCharacterEntryNoVa
)
