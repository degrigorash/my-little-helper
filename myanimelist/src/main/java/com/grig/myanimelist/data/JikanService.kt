package com.grig.myanimelist.data

import com.grig.myanimelist.data.model.jikan.JikanAnimeListResponse
import com.grig.myanimelist.data.model.jikan.JikanCharacterFullResponse
import com.grig.myanimelist.data.model.jikan.JikanCharactersResponse
import com.grig.myanimelist.data.model.jikan.JikanPersonFullResponse
import com.grig.myanimelist.data.model.jikan.JikanProducerResponse
import com.grig.myanimelist.data.model.jikan.JikanRelationsResponse
import com.grig.myanimelist.data.model.jikan.JikanReviewsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Catalogue endpoints in the Jikan v4 schema. Served primarily by Tenrai
 * (base URL `api.tenrai.org/v1/`), with [com.grig.myanimelist.data.setup.TenraiFallbackInterceptor]
 * transparently replaying failed requests against Jikan (`api.jikan.moe/v4/`) —
 * both APIs share this schema, so paths here are version-less.
 */
interface JikanService {

    @GET("anime/{id}/relations")
    suspend fun getAnimeRelations(
        @Path("id") animeId: Int
    ): Result<JikanRelationsResponse>

    @GET("manga/{id}/relations")
    suspend fun getMangaRelations(
        @Path("id") mangaId: Int
    ): Result<JikanRelationsResponse>

    @GET("anime/{id}/reviews")
    suspend fun getAnimeReviews(
        @Path("id") animeId: Int,
        @Query("page") page: Int = 1
    ): Result<JikanReviewsResponse>

    @GET("manga/{id}/reviews")
    suspend fun getMangaReviews(
        @Path("id") mangaId: Int,
        @Query("page") page: Int = 1
    ): Result<JikanReviewsResponse>

    @GET("anime/{id}/characters")
    suspend fun getAnimeCharacters(
        @Path("id") animeId: Int
    ): Result<JikanCharactersResponse>

    @GET("manga/{id}/characters")
    suspend fun getMangaCharacters(
        @Path("id") mangaId: Int
    ): Result<JikanCharactersResponse>

    @GET("characters/{id}/full")
    suspend fun getCharacterFull(
        @Path("id") characterId: Int
    ): Result<JikanCharacterFullResponse>

    @GET("producers/{id}")
    suspend fun getProducer(
        @Path("id") producerId: Int
    ): Result<JikanProducerResponse>

    @GET("anime")
    suspend fun getAnimeByProducer(
        @Query("producers") producerId: Int,
        @Query("order_by") orderBy: String = "score",
        @Query("sort") sort: String = "desc",
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 25
    ): Result<JikanAnimeListResponse>

    @GET("people/{id}/full")
    suspend fun getPersonFull(
        @Path("id") personId: Int
    ): Result<JikanPersonFullResponse>

}
