package com.grig.myanimelist.data

import com.grig.myanimelist.data.model.MalTokenResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface MalAuthService {

    @FormUrlEncoded
    @POST("v1/oauth2/token")
    suspend fun getToken(
        @Field("client_id") clientId: String,
        @Field("grant_type") grantType: String,
        @Field("code") code: String,
        @Field("code_verifier") codeVerifier: String
    ): Result<MalTokenResponse>
}