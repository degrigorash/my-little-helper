package com.grig.myanimelist.data.model.manga

import kotlinx.serialization.SerialName

enum class MalMangaPublishStatus {
    @SerialName("finished")
    Finished,
    @SerialName("currently_publishing")
    CurrentlyPublishing,
    @SerialName("not_yet_published")
    NotYetPublished,
    @SerialName("on_hiatus")
    OnHiatus,
    @SerialName("discontinued")
    Discontinued
}