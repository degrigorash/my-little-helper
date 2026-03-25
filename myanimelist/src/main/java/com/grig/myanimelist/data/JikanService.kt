package com.grig.myanimelist.data

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

}
