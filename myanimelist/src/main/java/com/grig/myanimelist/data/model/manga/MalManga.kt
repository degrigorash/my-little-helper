package com.grig.myanimelist.data.model.manga

import com.grig.myanimelist.data.model.MalAlternativeTitles
import com.grig.myanimelist.data.model.MalGenre
import com.grig.myanimelist.data.model.MalNsfw
import com.grig.myanimelist.data.model.MalPictures
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MalManga(
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
    @SerialName("media_type")
    val mediaType: MalMangaMediaType = MalMangaMediaType.Manga,
    @SerialName("status")
    val status: MalMangaPublishStatus = MalMangaPublishStatus.CurrentlyPublishing,
    @SerialName("num_volumes")
    val numVolumes: Int? = null,
    @SerialName("num_chapters")
    val numChapters: Int? = null,
    @SerialName("start_date")
    val startDate: String? = null,
    @SerialName("end_date")
    val endDate: String? = null,
    @SerialName("authors")
    val authors: List<MalAuthorNode> = emptyList(),
    @SerialName("genres")
    val genres: List<MalGenre> = emptyList(),
    @SerialName("main_picture")
    val pictures: MalPictures? = null,
    @SerialName("nsfw")
    val nsfw: MalNsfw = MalNsfw.Safe,
    @SerialName("my_list_status")
    val myListStatus: MalMangaListStatus? = null,
    @SerialName("pictures")
    val gallery: List<MalPictures> = emptyList(),
    @SerialName("related_manga")
    val relatedManga: List<MalRelatedManga> = emptyList()
)

