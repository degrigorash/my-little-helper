package com.grig.myanimelist.data

import javax.inject.Inject

class MalRepository @Inject constructor(
    private val malService: MalService
) {

    suspend fun tryLogin(

    ) {
        malService.oauth2(
            "authorization_code",
            "code",
            "https://myanimelist.net/v1/oauth2/token",
            "https://myanimelist.net/v1/oauth2/token",
            "https://myanimelist.net/v1/oauth2/token",
            "https://myanimelist.net/v1/oauth2/token"
        )
    }
}