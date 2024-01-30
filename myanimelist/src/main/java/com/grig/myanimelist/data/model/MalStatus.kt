package com.grig.myanimelist.data.model

import kotlinx.serialization.SerialName

enum class MalStatus {
    @SerialName("finished_airing")
    FinishedAiring,
    @SerialName("currently_airing")
    CurrentlyAiring,
    @SerialName("not_yet_aired")
    NotYetAired
}