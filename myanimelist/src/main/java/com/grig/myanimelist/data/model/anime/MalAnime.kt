package com.grig.myanimelist.data.model.anime

import com.grig.myanimelist.data.model.MalAlternativeTitles
import com.grig.myanimelist.data.model.MalNsfw
import com.grig.myanimelist.data.model.MalPictures
import com.grig.myanimelist.data.model.MalRating
import com.grig.myanimelist.data.model.MalStudio
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MalAnime(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("alternative_titles")
    val alternativeTitles: MalAlternativeTitles,
    @SerialName("synopsis")
    val synopsis: String,
    @SerialName("mean")
    val mean: Float? = null,
    @SerialName("rank")
    val rank: Int? = null,
    @SerialName("popularity")
    val popularity: Int? = null,
    @SerialName("status")
    val status: MalAnimeAiringStatus = MalAnimeAiringStatus.NotYetAired,
    @SerialName("num_episodes")
    val numEpisodes: Int? = null,
    @SerialName("studios")
    val studios: List<MalStudio> = emptyList(),
    @SerialName("main_picture")
    val pictures: MalPictures? = null,
    @SerialName("rating")
    val rating: MalRating? = null,
    @SerialName("nsfw")
    val nsfw: MalNsfw = MalNsfw.Safe
)