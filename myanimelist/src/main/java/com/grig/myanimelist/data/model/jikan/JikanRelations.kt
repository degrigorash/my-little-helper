package com.grig.myanimelist.data.model.jikan

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JikanRelationsResponse(
    @SerialName("data")
    val data: List<JikanRelationGroup>
)

@Serializable
data class JikanRelationGroup(
    @SerialName("relation")
    val relation: String,
    @SerialName("entry")
    val entry: List<JikanRelationEntry>
)

@Serializable
data class JikanRelationEntry(
    @SerialName("mal_id")
    val malId: Int,
    @SerialName("type")
    val type: String,
    @SerialName("name")
    val name: String
)

data class ResolvedRelation(
    val malId: Int,
    val name: String,
    val type: String,
    val relation: String,
    val imageUrl: String? = null,
    val year: String? = null
)
