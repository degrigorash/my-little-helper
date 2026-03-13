package com.grig.myanimelist.data.model.manga

import kotlinx.serialization.SerialName

enum class MalMangaMediaType(val displayName: String) {
    @SerialName("manga")
    Manga("Manga"),
    @SerialName("light_novel")
    LightNovel("Light Novel"),
    @SerialName("one_shot")
    OneShot("One-shot"),
    @SerialName("doujinshi")
    Doujinshi("Doujinshi"),
    @SerialName("manhwa")
    Manhwa("Manhwa"),
    @SerialName("manhua")
    Manhua("Manhua"),
    @SerialName("oel")
    Oel("OEL"),
    @SerialName("novel")
    Novel("Novel"),
    @SerialName("unknown")
    Unknown("Unknown")
}
