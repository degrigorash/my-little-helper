package com.grig.myanimelist.data

import com.grig.myanimelist.data.model.jikan.JikanDetailResponse
import com.grig.myanimelist.data.model.jikan.JikanRelationsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface JikanService {

    @GET("v4/anime/{id}/relations")
    suspend fun getAnimeRelations(
        @Path("id") animeId: Int
    ): Result<JikanRelationsResponse>

    @GET("v4/manga/{id}/relations")
    suspend fun getMangaRelations(
        @Path("id") mangaId: Int
    ): Result<JikanRelationsResponse>

    @GET("v4/anime/{id}")
    suspend fun getAnimeById(
        @Path("id") animeId: Int
    ): Result<JikanDetailResponse>

    @GET("v4/manga/{id}")
    suspend fun getMangaById(
        @Path("id") mangaId: Int
    ): Result<JikanDetailResponse>
}
