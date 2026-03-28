package com.grig.myanimelist.data.model.jikan

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JikanCharactersResponse(
    @SerialName("data")
    val data: List<JikanCharacterEntry>
)

@Serializable
data class JikanCharacterEntry(
    @SerialName("character")
    val character: JikanCharacterMeta,
    @SerialName("role")
    val role: String,
    @SerialName("voice_actors")
    val voiceActors: List<JikanVoiceActor> = emptyList()
)

@Serializable
data class JikanCharacterMeta(
    @SerialName("mal_id")
    val malId: Int,
    @SerialName("url")
    val url: String,
    @SerialName("images")
    val images: JikanCharacterImages? = null,
    @SerialName("name")
    val name: String
)

@Serializable
data class JikanCharacterImages(
    @SerialName("jpg")
    val jpg: JikanCharacterImageFormat? = null,
    @SerialName("webp")
    val webp: JikanCharacterImageFormat? = null
)

@Serializable
data class JikanCharacterImageFormat(
    @SerialName("image_url")
    val imageUrl: String? = null,
    @SerialName("small_image_url")
    val smallImageUrl: String? = null
)

@Serializable
data class JikanVoiceActor(
    @SerialName("person")
    val person: JikanPersonMeta,
    @SerialName("language")
    val language: String
)

@Serializable
data class JikanPersonMeta(
    @SerialName("mal_id")
    val malId: Int,
    @SerialName("url")
    val url: String,
    @SerialName("images")
    val images: JikanPersonImages? = null,
    @SerialName("name")
    val name: String
)

@Serializable
data class JikanPersonImages(
    @SerialName("jpg")
    val jpg: JikanPersonImageFormat? = null
)

@Serializable
data class JikanPersonImageFormat(
    @SerialName("image_url")
    val imageUrl: String? = null
)
