package com.grig.myanimelist.data

import com.grig.myanimelist.data.model.MalUser
import com.grig.myanimelist.data.model.anime.MalAnime
import com.grig.myanimelist.data.model.anime.MalAnimeList
import com.grig.myanimelist.data.model.anime.MalAnimeListStatus
import com.grig.myanimelist.data.model.manga.MalManga
import com.grig.myanimelist.data.model.manga.MalMangaList
import com.grig.myanimelist.data.model.manga.MalMangaListStatus
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface MalService {

    @GET("v2/users/@me")
    suspend fun getUser(): Result<MalUser>

    @GET("v2/anime")
    suspend fun searchAnime(
        @Query("q") query: String,
        @Query("limit") limit: Int = 15,
        @Query("fields") fields: String = "main_picture,start_date,end_date,status"
    ): Result<MalAnimeList>

    @GET("v2/anime/{anime_id}")
    suspend fun getAnimeDetails(
        @Path("anime_id") animeId: Int,
        @Query("fields") fields: String = "alternative_titles,synopsis,mean,rank,popularity,num_list_users,status,num_episodes,start_date,end_date,genres,studios,main_picture,rating,nsfw,my_list_status{status,score,num_episodes_watched,finish_date}"
    ): Result<MalAnime>

    @GET("v2/manga")
    suspend fun searchManga(
        @Query("q") query: String,
        @Query("limit") limit: Int = 15,
        @Query("fields") fields: String = "main_picture,start_date,end_date,status"
    ): Result<MalMangaList>

    @GET("v2/manga/{manga_id}")
    suspend fun getMangaDetails(
        @Path("manga_id") mangaId: Int,
        @Query("fields") fields: String = "alternative_titles,synopsis,mean,rank,popularity,num_list_users,media_type,status,num_volumes,num_chapters,start_date,end_date,authors{first_name,last_name},genres,main_picture,nsfw,my_list_status{status,score,num_chapters_read,num_volumes_read,finish_date}"
    ): Result<MalManga>

    @GET("v2/users/{username}/animelist")
    suspend fun getUserAnimeList(
        @Path("username") username: String,
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0,
        @Query("nsfw") nsfw: Boolean = true,
        @Query("status") status: String? = null,
        @Query("fields") fields: String = "list_status{status,score,num_episodes_watched,finish_date},alternative_titles,synopsis,mean,rank,popularity,num_list_users,status,num_episodes,start_date,end_date,genres,studios,pictures,rating,nsfw"
    ): Result<MalAnimeList>

    @FormUrlEncoded
    @PATCH("v2/anime/{anime_id}/my_list_status")
    suspend fun updateAnimeListStatus(
        @Path("anime_id") animeId: Int,
        @Field("status") status: String? = null,
        @Field("score") score: Int? = null,
        @Field("num_watched_episodes") numWatchedEpisodes: Int? = null,
        @Field("finish_date") finishDate: String? = null
    ): Result<MalAnimeListStatus>

    @DELETE("v2/anime/{anime_id}/my_list_status")
    suspend fun deleteAnimeListItem(
        @Path("anime_id") animeId: Int
    ): Result<Unit>

    @GET("v2/users/{username}/mangalist")
    suspend fun getUserMangaList(
        @Path("username") username: String,
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0,
        @Query("nsfw") nsfw: Boolean = true,
        @Query("status") status: String? = null,
        @Query("fields") fields: String = "list_status{status,score,num_chapters_read,num_volumes_read,finish_date},alternative_titles,synopsis,mean,rank,popularity,num_list_users,media_type,status,num_volumes,num_chapters,start_date,end_date,authors{first_name,last_name},genres,pictures,nsfw"
    ): Result<MalMangaList>

    @FormUrlEncoded
    @PATCH("v2/manga/{manga_id}/my_list_status")
    suspend fun updateMangaListStatus(
        @Path("manga_id") mangaId: Int,
        @Field("status") status: String? = null,
        @Field("score") score: Int? = null,
        @Field("num_chapters_read") numChaptersRead: Int? = null,
        @Field("num_volumes_read") numVolumesRead: Int? = null,
        @Field("finish_date") finishDate: String? = null
    ): Result<MalMangaListStatus>

    @DELETE("v2/manga/{manga_id}/my_list_status")
    suspend fun deleteMangaListItem(
        @Path("manga_id") mangaId: Int
    ): Result<Unit>
}