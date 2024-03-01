package com.grig.myanimelist.data.model.manga

import kotlinx.serialization.SerialName

enum class MalMangaReadingStatus {
    @SerialName("reading")
    Reading,
    @SerialName("completed")
    Completed,
    @SerialName("on_hold")
    OnHold,
    @SerialName("dropped")
    Dropped,
    @SerialName("plan_to_read")
    PlanToRead
}