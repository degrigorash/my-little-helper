package com.grig.myanimelist.data

import com.grig.myanimelist.data.model.MalUser
import com.grig.myanimelist.data.model.anime.MalAnimeList
import com.grig.myanimelist.data.model.manga.MalMangaList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MalService {

    @GET("v2/users/@me")
    suspend fun getUser(): Result<MalUser>

    @GET("v2/users/{username}/animelist")
    suspend fun getUserAnimeList(
        @Path("username") username: String,
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0,
        @Query("nsfw") nsfw: Boolean = true,
        @Query("status") status: String? = null,
        @Query("fields") fields: String = "list_status{status,score,num_episodes_watched},alternative_titles,synopsis,mean,rank,popularity,num_list_users,status,num_episodes,start_date,end_date,genres,studios,pictures,rating,nsfw"
    ): Result<MalAnimeList>

    @GET("v2/users/{username}/mangalist")
    suspend fun getUserMangaList(
        @Path("username") username: String,
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0,
        @Query("nsfw") nsfw: Boolean = true,
        @Query("status") status: String? = null,
        @Query("fields") fields: String = "list_status{status,score,num_chapters_read,num_volumes_read},alternative_titles,synopsis,mean,rank,popularity,num_list_users,media_type,status,num_volumes,num_chapters,start_date,end_date,authors{first_name,last_name},genres,pictures,nsfw"
    ): Result<MalMangaList>
}