package com.grig.myanimelist.data.model.anime

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MalRelatedAnime(
    @SerialName("node")
    val node: MalAnime,
    @SerialName("relation_type")
    val relationType: String,
    @SerialName("relation_type_formatted")
    val relationTypeFormatted: String
)
