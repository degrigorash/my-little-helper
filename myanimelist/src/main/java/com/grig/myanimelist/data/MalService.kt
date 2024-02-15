package com.grig.myanimelist.data

import com.grig.myanimelist.data.model.anime.MalAnimeList
import com.grig.myanimelist.data.model.MalUser
import com.grig.myanimelist.data.model.manga.MalMangaList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MalService {

    @GET("v2/users/@me")
    suspend fun getUser(): Response<MalUser>

    @GET("v2/users/{username}/animelist")
    suspend fun getUserAnimeList(
        @Path("username") username: String,
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0,
        @Query("nsfw") nsfw: Boolean = true,
        @Query("status") status: String = "plan_to_watch",
        @Query("fields") fields: String = "alternative_titles,synopsis,mean,rank,popularity,status,num_episodes,studios,pictures,rating,nsfw"
    ): Response<MalAnimeList>

    @GET("v2/users/{username}/mangalist")
    suspend fun getUserMangaList(
        @Path("username") username: String,
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0,
        @Query("nsfw") nsfw: Boolean = true,
        @Query("status") status: String = "plan_to_read",
        @Query("fields") fields: String = "alternative_titles,synopsis,mean,rank,popularity,media_type,status,num_volumes,num_chapters,pictures,nsfw"
    ): Response<MalMangaList>
}