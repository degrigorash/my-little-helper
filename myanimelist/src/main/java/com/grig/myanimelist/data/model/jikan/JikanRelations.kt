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
    val year: String? = null,
    /**
     * Precise media-type label resolved from the entry's own MAL record
     * (e.g. "Light Novel", "Manga", "TV", "Movie"). Jikan's relation [type]
     * is too coarse to tell a manga from a light novel, so this is read from
     * the resolved [com.grig.myanimelist.data.model.manga.MalManga.mediaType]
     * / [com.grig.myanimelist.data.model.anime.MalAnime.mediaType]. Null when
     * the detail lookup failed.
     */
    val mediaTypeLabel: String? = null,
    /**
     * Airing/publishing status of the related entry, read from the resolved
     * MAL record's status enum (e.g. "Finished", "Airing", "Publishing").
     * Null when the detail lookup failed.
     */
    val statusLabel: String? = null
)
