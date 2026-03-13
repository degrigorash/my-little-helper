package com.grig.myanimelist.data.model.manga

import kotlinx.serialization.SerialName

enum class MalMangaPublishStatus(val displayName: String) {
    @SerialName("finished")
    Finished("Finished"),
    @SerialName("currently_publishing")
    CurrentlyPublishing("Publishing"),
    @SerialName("not_yet_published")
    NotYetPublished("Upcoming"),
    @SerialName("on_hiatus")
    OnHiatus("Hiatus"),
    @SerialName("discontinued")
    Discontinued("Discontinued")
}
