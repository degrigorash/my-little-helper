package com.grig.danish.data

import com.grig.danish.data.model.Noun
import retrofit2.http.GET

interface DanishService {

    @GET("api/danish/nouns/getAll")
    suspend fun getAllNouns(): Result<List<Noun>>
}
