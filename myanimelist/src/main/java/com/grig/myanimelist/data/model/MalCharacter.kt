package com.grig.myanimelist.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Minimal MAL API v2 character node. MAL character ids match Jikan's, so this is
 * used to enrich Jikan data (e.g. the person screen) with [numFavorites], which the
 * Jikan /people/{id}/voices data doesn't provide.
 */
@Serializable
data class MalCharacter(
    @SerialName("id")
    val id: Int,
    @SerialName("num_favorites")
    val numFavorites: Int = 0
)
