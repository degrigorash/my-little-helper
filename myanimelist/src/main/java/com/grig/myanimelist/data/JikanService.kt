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

interface JikanService {

    @GET("v4/anime/{id}/relations")
    suspend fun getAnimeRelations(
        @Path("id") animeId: Int
    ): Result<JikanRelationsResponse>

    @GET("v4/manga/{id}/relations")
    suspend fun getMangaRelations(
        @Path("id") mangaId: Int
    ): Result<JikanRelationsResponse>

    @GET("v4/anime/{id}/reviews")
    suspend fun getAnimeReviews(
        @Path("id") animeId: Int,
        @Query("page") page: Int = 1
    ): Result<JikanReviewsResponse>

    @GET("v4/manga/{id}/reviews")
    suspend fun getMangaReviews(
        @Path("id") mangaId: Int,
        @Query("page") page: Int = 1
    ): Result<JikanReviewsResponse>

    @GET("v4/anime/{id}/characters")
    suspend fun getAnimeCharacters(
        @Path("id") animeId: Int
    ): Result<JikanCharactersResponse>

    @GET("v4/manga/{id}/characters")
    suspend fun getMangaCharacters(
        @Path("id") mangaId: Int
    ): Result<JikanCharactersResponse>

    @GET("v4/characters/{id}/full")
    suspend fun getCharacterFull(
        @Path("id") characterId: Int
    ): Result<JikanCharacterFullResponse>

    @GET("v4/producers/{id}")
    suspend fun getProducer(
        @Path("id") producerId: Int
    ): Result<JikanProducerResponse>

    @GET("v4/anime")
    suspend fun getAnimeByProducer(
        @Query("producers") producerId: Int,
        @Query("order_by") orderBy: String = "score",
        @Query("sort") sort: String = "desc",
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 25
    ): Result<JikanAnimeListResponse>

    @GET("v4/people/{id}/full")
    suspend fun getPersonFull(
        @Path("id") personId: Int
    ): Result<JikanPersonFullResponse>

}
