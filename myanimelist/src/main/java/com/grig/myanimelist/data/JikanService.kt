package com.grig.myanimelist.data

import com.grig.myanimelist.data.model.jikan.JikanCharacterFullResponse
import com.grig.myanimelist.data.model.jikan.JikanCharactersResponse
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

}
