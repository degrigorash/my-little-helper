package com.grig.myanimelist.data.model.manga

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MalMangaListStatus(
    @SerialName("status")
    val status: MalMangaReadingStatus = MalMangaReadingStatus.PlanToRead
)
