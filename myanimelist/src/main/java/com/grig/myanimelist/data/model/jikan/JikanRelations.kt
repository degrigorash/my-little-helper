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

@Serializable
data class JikanDetailResponse(
    @SerialName("data")
    val data: JikanDetailData
)

@Serializable
data class JikanDetailData(
    @SerialName("mal_id")
    val malId: Int,
    @SerialName("images")
    val images: JikanImages? = null
)

@Serializable
data class JikanImages(
    @SerialName("jpg")
    val jpg: JikanImageUrls? = null
)

@Serializable
data class JikanImageUrls(
    @SerialName("image_url")
    val imageUrl: String? = null,
    @SerialName("small_image_url")
    val smallImageUrl: String? = null,
    @SerialName("large_image_url")
    val largeImageUrl: String? = null
)

data class ResolvedRelation(
    val malId: Int,
    val name: String,
    val type: String,
    val relation: String,
    val imageUrl: String? = null
)
