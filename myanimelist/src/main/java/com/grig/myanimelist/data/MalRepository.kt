package com.grig.myanimelist.data

import android.net.Uri
import com.grig.myanimelist.data.model.MalUser
import com.grig.myanimelist.tools.generateCodeVerifier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MalRepository @Inject constructor(
    private val malAuthService: MalAuthService,
    private val malService: MalService,
    private val tokenManager: TokenManager
) {

    val userFlow: Flow<MalUser?> = tokenManager.accessTokenFlow
        .map { accessToken ->
            if (!accessToken.isNullOrBlank()) {
                val response = malService.getUser()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    null
                }
            } else {
                null
            }
        }

    private val codeVerifier by lazy { generateCodeVerifier() }
    private val codeChallenge by lazy { codeVerifier }
    private val clientId = "2af73157ee73b08de87664cbf7cfb162"

    fun loginUri() = "https://myanimelist.net/v1/oauth2/authorize?" +
        "response_type=code" +
        "&client_id=$clientId" +
        "&code_challenge=$codeChallenge" +
        "&code_challenge_method=plain"

    suspend fun getToken(authorizationCode: String) {
        val response = malAuthService.getToken(
            clientId = clientId,
            code = authorizationCode,
            codeVerifier = codeVerifier,
            grantType = "authorization_code"
        )
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                tokenManager.saveTokens(body.accessToken, body.refreshToken)
            }
        }
    }

    fun getAuthorizationCode(uri: Uri?): String? {
        return uri?.getQueryParameter("code")
    }

    companion object {
        const val MAL_AUTH_REDIRECT_HOST = "grigmal.auth"
    }
}