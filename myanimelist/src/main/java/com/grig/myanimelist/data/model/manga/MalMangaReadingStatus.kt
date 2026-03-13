package com.grig.myanimelist.data.model.manga

import kotlinx.serialization.SerialName

enum class MalMangaReadingStatus(val apiValue: String, val displayName: String) {
    @SerialName("reading")
    Reading("reading", "Reading"),
    @SerialName("completed")
    Completed("completed", "Completed"),
    @SerialName("on_hold")
    OnHold("on_hold", "On Hold"),
    @SerialName("dropped")
    Dropped("dropped", "Dropped"),
    @SerialName("plan_to_read")
    PlanToRead("plan_to_read", "Plan to Read")
}
