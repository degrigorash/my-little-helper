package com.grig.myanimelist.data

import com.grig.myanimelist.data.model.MalUser
import retrofit2.Response
import retrofit2.http.GET

interface MalService {

    @GET("v2/users/@me")
    suspend fun getUser(): Response<MalUser>
}