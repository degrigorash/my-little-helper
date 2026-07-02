package com.grig.myanimelist.data.model.jikan

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JikanPersonVoiceRole(
    @SerialName("role")
    val role: String,
    @SerialName("anime")
    val anime: JikanAnimeMeta,
    @SerialName("character")
    val character: JikanCharacterMeta
)

/**
 * Combined person detail: the person's profile plus the characters they voiced,
 * each grouping all anime the character appears in.
 * Not a wire type — assembled by [com.grig.myanimelist.data.MalRepository].
 */
data class PersonDetail(
    val person: JikanPersonFull,
    val characters: List<VoicedCharacter>
)

/**
 * A character voiced by the person, with every anime they appear in and the
 * character's MAL favorites count (fetched from the MAL v2 API by [character.malId]).
 */
data class VoicedCharacter(
    val character: JikanCharacterMeta,
    val favorites: Int,
    val roles: List<VoicedCharacterAnime>
)

data class VoicedCharacterAnime(
    val anime: JikanAnimeMeta,
    val role: String
)
