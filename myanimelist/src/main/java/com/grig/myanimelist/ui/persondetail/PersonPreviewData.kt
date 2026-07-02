package com.grig.myanimelist.ui.persondetail

import com.grig.myanimelist.data.model.jikan.JikanAnimeMeta
import com.grig.myanimelist.data.model.jikan.JikanCharacterMeta
import com.grig.myanimelist.data.model.jikan.JikanPersonFull
import com.grig.myanimelist.data.model.jikan.PersonDetail
import com.grig.myanimelist.data.model.jikan.VoicedCharacter
import com.grig.myanimelist.data.model.jikan.VoicedCharacterAnime

val previewVoicedCharacters = listOf(
    VoicedCharacter(
        character = JikanCharacterMeta(
            malId = 2308,
            url = "",
            name = "Tsukino, Usagi"
        ),
        favorites = 8421,
        roles = listOf(
            VoicedCharacterAnime(
                anime = JikanAnimeMeta(malId = 530, url = "", title = "Bishoujo Senshi Sailor Moon"),
                role = "Main"
            ),
            VoicedCharacterAnime(
                anime = JikanAnimeMeta(malId = 42821, url = "", title = "Bishoujo Senshi Sailor Moon Cosmos Movie"),
                role = "Main"
            )
        )
    ),
    VoicedCharacter(
        character = JikanCharacterMeta(
            malId = 83,
            url = "",
            name = "Katsuragi, Misato"
        ),
        favorites = 5310,
        roles = listOf(
            VoicedCharacterAnime(
                anime = JikanAnimeMeta(malId = 30, url = "", title = "Neon Genesis Evangelion"),
                role = "Main"
            )
        )
    )
)

val previewPersonDetail = PersonDetail(
    person = JikanPersonFull(
        malId = 9,
        name = "Kotono Mitsuishi",
        givenName = "琴乃",
        familyName = "三石",
        birthday = "1967-12-08T00:00:00+00:00",
        favorites = 2296,
        about = "Kotono Mitsuishi is a Japanese actress and voice actress."
    ),
    characters = previewVoicedCharacters
)
