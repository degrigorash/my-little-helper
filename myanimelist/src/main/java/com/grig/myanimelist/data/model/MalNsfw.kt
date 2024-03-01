package com.grig.myanimelist.data.model

import kotlinx.serialization.SerialName

enum class MalNsfw {
    @SerialName("white")
    Safe,
    @SerialName("gray")
    Suggestive,
    @SerialName("black")
    Nsfw
}