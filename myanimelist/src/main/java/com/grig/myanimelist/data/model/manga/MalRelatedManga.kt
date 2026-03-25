package com.grig.myanimelist.data.model.manga

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MalRelatedManga(
    @SerialName("node")
    val node: MalManga,
    @SerialName("relation_type")
    val relationType: String,
    @SerialName("relation_type_formatted")
    val relationTypeFormatted: String
)
