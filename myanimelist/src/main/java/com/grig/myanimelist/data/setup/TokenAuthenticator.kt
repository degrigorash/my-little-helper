package com.grig.myanimelist.data.setup

import com.grig.myanimelist.clientApiId
import com.grig.myanimelist.data.MalAuthService
import com.grig.myanimelist.data.UserManager
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber

internal class TokenAuthenticator(
    private val userManager: UserManager,
    private val malAuthService: MalAuthService
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // Avoid infinite retry loop
        if (response.priorResponse != null) return null

        val refreshToken = userManager.refreshToken ?: run {
            Timber.w("No refresh token available, logging out")
            return null
        }

        val newTokens = runBlocking {
            malAuthService.refreshToken(
                clientId = clientApiId,
                grantType = "refresh_token",
                refreshToken = refreshToken
            ).getOrNull()
        }

        if (newTokens == null) {
            Timber.w("Token refresh failed, logging out")
            runBlocking { userManager.logout() }
            return null
        }

        runBlocking {
            userManager.saveTokens(newTokens.accessToken, newTokens.refreshToken)
        }

        return response.request.newBuilder()
            .header("Authorization", "Bearer ${newTokens.accessToken}")
            .build()
    }
}
