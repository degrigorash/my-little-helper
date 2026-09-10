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
    val name: String,
    /**
     * Tenrai enriches relation entries with the entry's media type (e.g. "Manga",
     * "Light Novel") and images inline; Jikan does not, so both are null when the
     * response came from the Jikan fallback.
     */
    @SerialName("media_type")
    val mediaType: String? = null,
    @SerialName("images")
    val images: JikanRelationImages? = null
)

@Serializable
data class JikanRelationImages(
    @SerialName("jpg")
    val jpg: JikanRelationImageFormat? = null
)

@Serializable
data class JikanRelationImageFormat(
    @SerialName("image_url")
    val imageUrl: String? = null,
    @SerialName("large_image_url")
    val largeImageUrl: String? = null
)

data class ResolvedRelation(
    val malId: Int,
    val name: String,
    val type: String,
    val relation: String,
    val imageUrl: String? = null,
    val year: String? = null,
    /**
     * Precise media-type label resolved from the entry's own MAL record
     * (e.g. "Light Novel", "Manga", "TV", "Movie"). The relation [type] is
     * too coarse to tell a manga from a light novel, so this is read from
     * the resolved [com.grig.myanimelist.data.model.manga.MalManga.mediaType]
     * / [com.grig.myanimelist.data.model.anime.MalAnime.mediaType], falling
     * back to Tenrai's inline [JikanRelationEntry.mediaType]. Null when both
     * are unavailable.
     */
    val mediaTypeLabel: String? = null,
    /**
     * Airing/publishing status of the related entry, read from the resolved
     * MAL record's status enum (e.g. "Finished", "Airing", "Publishing").
     * Null when the detail lookup failed.
     */
    val statusLabel: String? = null
)
