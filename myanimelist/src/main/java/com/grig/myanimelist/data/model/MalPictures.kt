package com.grig.myanimelist.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MalPictures(
    @SerialName("medium")
    val medium: String? = null,
    @SerialName("large")
    val large: String? = null
)
