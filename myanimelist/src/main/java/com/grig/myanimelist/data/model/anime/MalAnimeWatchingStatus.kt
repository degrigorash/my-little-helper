package com.grig.myanimelist.data.model.anime

import kotlinx.serialization.SerialName

enum class MalAnimeWatchingStatus {
    @SerialName("watching")
    Watching,
    @SerialName("completed")
    Completed,
    @SerialName("on_hold")
    OnHold,
    @SerialName("dropped")
    Dropped,
    @SerialName("plan_to_watch")
    PlanToWatch
}