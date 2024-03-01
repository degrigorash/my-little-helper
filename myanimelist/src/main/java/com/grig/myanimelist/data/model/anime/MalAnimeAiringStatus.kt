package com.grig.myanimelist.data.model.anime

import kotlinx.serialization.SerialName

enum class MalAnimeAiringStatus {
    @SerialName("finished_airing")
    FinishedAiring,
    @SerialName("currently_airing")
    CurrentlyAiring,
    @SerialName("not_yet_aired")
    NotYetAired
}