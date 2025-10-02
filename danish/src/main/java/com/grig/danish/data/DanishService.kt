package com.grig.danish.data

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Placeholder service for Danish features. Can be extended with real endpoints later.
 */
interface DanishService {

    @GET("ddo/ordbog")
    suspend fun word(@Query("query") query: String): Result<String>
}
