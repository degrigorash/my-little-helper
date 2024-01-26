package com.grig.myanimelist.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MalTokenRequest(
    @SerialName("client_id")
    val clientId: String,
    @SerialName("grant_type")
    val grantType: String,
    @SerialName("code")
    val code: String,
    @SerialName("code_verifier")
    val codeVerifier: String
)