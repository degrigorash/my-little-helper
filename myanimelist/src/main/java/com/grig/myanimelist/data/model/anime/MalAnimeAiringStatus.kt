package com.grig.myanimelist.data.model.anime

import kotlinx.serialization.SerialName

enum class MalAnimeAiringStatus(val displayName: String) {
    @SerialName("finished_airing")
    FinishedAiring("Finished"),
    @SerialName("currently_airing")
    CurrentlyAiring("Airing"),
    @SerialName("not_yet_aired")
    NotYetAired("Upcoming")
}
