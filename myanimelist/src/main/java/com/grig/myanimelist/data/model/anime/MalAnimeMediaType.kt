package com.grig.myanimelist.data.model.anime

import kotlinx.serialization.SerialName

enum class MalAnimeMediaType(val displayName: String) {
    @SerialName("tv")
    Tv("TV"),
    @SerialName("ova")
    Ova("OVA"),
    @SerialName("movie")
    Movie("Movie"),
    @SerialName("special")
    Special("Special"),
    @SerialName("tv_special")
    TvSpecial("TV Special"),
    @SerialName("ona")
    Ona("ONA"),
    @SerialName("music")
    Music("Music"),
    @SerialName("cm")
    Cm("CM"),
    @SerialName("pv")
    Pv("PV"),
    @SerialName("unknown")
    Unknown("Unknown")
}
