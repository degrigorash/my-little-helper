package com.grig.myanimelist.data.model.anime

import com.grig.myanimelist.data.model.MalAlternativeTitles
import com.grig.myanimelist.data.model.MalGenre
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
    val alternativeTitles: MalAlternativeTitles = MalAlternativeTitles(),
    @SerialName("synopsis")
    val synopsis: String = "",
    @SerialName("mean")
    val mean: Float? = null,
    @SerialName("rank")
    val rank: Int? = null,
    @SerialName("popularity")
    val popularity: Int? = null,
    @SerialName("num_list_users")
    val numListUsers: Int? = null,
    @SerialName("status")
    val status: MalAnimeAiringStatus = MalAnimeAiringStatus.NotYetAired,
    @SerialName("num_episodes")
    val numEpisodes: Int? = null,
    @SerialName("genres")
    val genres: List<MalGenre> = emptyList(),
    @SerialName("studios")
    val studios: List<MalStudio> = emptyList(),
    @SerialName("start_date")
    val startDate: String? = null,
    @SerialName("end_date")
    val endDate: String? = null,
    @SerialName("main_picture")
    val pictures: MalPictures? = null,
    @SerialName("rating")
    val rating: MalRating? = null,
    @SerialName("nsfw")
    val nsfw: MalNsfw = MalNsfw.Safe,
    @SerialName("my_list_status")
    val myListStatus: MalAnimeListStatus? = null,
    @SerialName("pictures")
    val gallery: List<MalPictures> = emptyList(),
    @SerialName("related_anime")
    val relatedAnime: List<MalRelatedAnime> = emptyList()
)