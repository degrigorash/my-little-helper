package com.grig.myanimelist.data.model.manga

import kotlinx.serialization.SerialName

enum class MalMangaMediaType {
    @SerialName("manga")
    Manga,
    @SerialName("light_novel")
    LightNovel,
    @SerialName("one_shot")
    OneShot,
    @SerialName("doujinshi")
    Doujinshi,
    @SerialName("manhwa")
    Manhwa,
    @SerialName("manhua")
    Manhua,
    @SerialName("oel")
    Oel,
    @SerialName("unknown")
    Unknown
}
