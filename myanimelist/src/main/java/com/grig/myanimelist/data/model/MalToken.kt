package com.grig.myanimelist.data.model

internal sealed class MalToken {
    data class Token(
        val accessToken: String,
        val refreshToken: String
    ) : MalToken()

    object Unauthorized : MalToken()
}
