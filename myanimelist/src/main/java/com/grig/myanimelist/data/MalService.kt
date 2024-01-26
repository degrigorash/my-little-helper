package com.grig.myanimelist.data

import retrofit2.http.GET
import retrofit2.http.Query

interface MalService {

    @GET("v1/oauth2/authorize")
    suspend fun oauth2(
        @Query("response_type") responseType: String,
        @Query("client_id") clientId: String,
        @Query("state") state: String,
        @Query("redirect_uri") redirectUri: String,
        @Query("code_challenge") codeChallenge: String,
        @Query("code_challenge_method") codeChallengeMethod: String
    ): String
}