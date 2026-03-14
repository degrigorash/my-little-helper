package com.grig.myanimelist.data.model.manga

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MalMangaListStatus(
    @SerialName("status")
    val status: MalMangaReadingStatus = MalMangaReadingStatus.PlanToRead,
    @SerialName("score")
    val score: Int? = null,
    @SerialName("num_chapters_read")
    val numChaptersRead: Int? = null,
    @SerialName("num_volumes_read")
    val numVolumesRead: Int? = null,
    @SerialName("finish_date")
    val finishDate: String? = null
)
