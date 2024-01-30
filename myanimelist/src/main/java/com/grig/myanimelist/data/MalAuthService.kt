package com.grig.myanimelist.data

import com.grig.myanimelist.data.model.MalTokenResponse
import com.grig.myanimelist.data.model.MalUser
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface MalAuthService {

    @FormUrlEncoded
    @POST("v1/oauth2/token")
    suspend fun getToken(
        @Field("client_id") clientId: String,
        @Field("grant_type") grantType: String,
        @Field("code") code: String,
        @Field("code_verifier") codeVerifier: String
    ): Response<MalTokenResponse>
}