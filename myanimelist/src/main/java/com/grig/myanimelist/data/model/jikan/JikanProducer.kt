package com.grig.myanimelist.data.model.jikan

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JikanProducerResponse(
    @SerialName("data")
    val data: JikanProducer
)

@Serializable
data class JikanProducer(
    @SerialName("mal_id")
    val malId: Int,
    @SerialName("titles")
    val titles: List<JikanProducerTitle> = emptyList(),
    @SerialName("images")
    val images: JikanProducerImages? = null,
    @SerialName("favorites")
    val favorites: Int = 0,
    @SerialName("established")
    val established: String? = null,
    @SerialName("about")
    val about: String? = null,
    @SerialName("count")
    val count: Int = 0
) {
    val name: String
        get() = titles.firstOrNull { it.type == "Default" }?.title
            ?: titles.firstOrNull()?.title
            ?: ""

    val japaneseName: String?
        get() = titles.firstOrNull { it.type == "Japanese" }?.title
}

@Serializable
data class JikanProducerTitle(
    @SerialName("type")
    val type: String,
    @SerialName("title")
    val title: String
)

@Serializable
data class JikanProducerImages(
    @SerialName("jpg")
    val jpg: JikanProducerImageFormat? = null
)

@Serializable
data class JikanProducerImageFormat(
    @SerialName("image_url")
    val imageUrl: String? = null
)
