package com.grig.myanimelist.data.model.anime

import kotlinx.serialization.SerialName

enum class MalAnimeWatchingStatus(val apiValue: String, val displayName: String) {
    @SerialName("watching")
    Watching("watching", "Watching"),
    @SerialName("completed")
    Completed("completed", "Completed"),
    @SerialName("on_hold")
    OnHold("on_hold", "On Hold"),
    @SerialName("dropped")
    Dropped("dropped", "Dropped"),
    @SerialName("plan_to_watch")
    PlanToWatch("plan_to_watch", "Plan to Watch")
}
