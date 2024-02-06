package com.grig.myanimelist.data

import com.grig.myanimelist.data.model.anime.MalAnimeList
import com.grig.myanimelist.data.model.MalUser
import com.grig.myanimelist.data.model.manga.MalMangaList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MalService {

    @GET("v2/users/@me")
    suspend fun getUser(): Response<MalUser>

    @GET("v2/users/degrigorash/animelist")
    suspend fun getUserAnimeList(
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0,
        @Query("status") status: String = "plan_to_watch",
        @Query("fields") fields: String = "alternative_titles,synopsis,mean,rank,popularity,status,num_episodes,studios,pictures"
    ): Response<MalAnimeList>

    @GET("v2/users/degrigorash/mangalist")
    suspend fun getUserMangaList(
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0,
        @Query("status") status: String = "plan_to_read",
        @Query("fields") fields: String = "alternative_titles,synopsis,mean,rank,popularity,media_type,status,num_volumes,num_chapters,pictures"
    ): Response<MalMangaList>
}