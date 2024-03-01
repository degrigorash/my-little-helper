package com.grig.myanimelist.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MalAlternativeTitles(
    @SerialName("synonyms")
    val synonyms: List<String> = emptyList(),
    @SerialName("en")
    val en: String? = null,
    @SerialName("ja")
    val ja: String? = null
)
