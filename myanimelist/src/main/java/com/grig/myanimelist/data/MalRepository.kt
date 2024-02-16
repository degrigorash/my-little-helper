package com.grig.myanimelist.data

import android.net.Uri
import com.grig.myanimelist.clientApiId
import com.grig.myanimelist.data.model.MalUserState
import com.grig.myanimelist.data.model.anime.MalAnime
import com.grig.myanimelist.data.model.anime.MalAnimeList
import com.grig.myanimelist.data.model.manga.MalManga
import com.grig.myanimelist.tools.generateCodeVerifier
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MalRepository @Inject constructor(
    private val malAuthService: MalAuthService,
    private val malService: MalService,
    private val userManager: UserManager
) {

    val userFlow: Flow<MalUserState> = userManager.userFlow

    private val codeVerifier by lazy { generateCodeVerifier() }
    private val codeChallenge by lazy { codeVerifier }

    fun loginUri() = "https://myanimelist.net/v1/oauth2/authorize?" +
        "response_type=code" +
        "&client_id=$clientApiId" +
        "&code_challenge=$codeChallenge" +
        "&code_challenge_method=plain"

    suspend fun auth(authorizationCode: String) {
        val response = malAuthService.getToken(
            clientId = clientApiId,
            code = authorizationCode,
            codeVerifier = codeVerifier,
            grantType = "authorization_code"
        )
        response.getOrNull()?.let {
            userManager.saveTokens(it.accessToken, it.refreshToken)
        } ?: {
            Timber.e("fail to get token: ${response.exceptionOrNull()?.message}")
        }

        val userResponse = malService.getUser()
        userResponse.getOrNull()?.let {
            userManager.saveUser(it)
        } ?: {
            Timber.e("fail to get user: ${userResponse.exceptionOrNull()?.message}")
        }
    }

    fun getAuthorizationCode(uri: Uri?): String? {
        return uri?.getQueryParameter("code")
    }

    suspend fun getUserAnimeList(
        username: String?,
        offset: Int
    ) = malService.getUserAnimeList(
        username = username ?: "@me",
        offset = offset
    )

    suspend fun getUserMangaList(
        username: String?,
        offset: Int
    ) = malService.getUserMangaList(
        username = username ?: "@me",
        offset = offset
    )

    // suspend fun getUserAnimeList(username: String?): List<MalAnime> {
    //     val result = mutableListOf<MalAnime>()
    //     var offset = 0
    //     var response = malService.getUserAnimeList(
    //         username = username ?: "@me",
    //         offset = offset
    //     )
    //     while (response.isSuccess) {
    //         val body = response.getOrNull()
    //         if (body?.data == null) break
    //         result.addAll(body.data.map { it.anime })
    //         offset += body.data.size
    //         response = malService.getUserAnimeList(
    //             username = username ?: "@me",
    //             offset = offset
    //         )
    //         if (body.data.size < 100) break
    //     }
    //     return result.sortedByDescending { it.mean }
    // }

    // suspend fun getUserMangaList(username: String?): List<MalManga> {
    //     val result = mutableListOf<MalManga>()
    //     var offset = 0
    //     var response = malService.getUserMangaList(
    //         username = username ?: "@me",
    //         offset = offset
    //     )
    //     while (response.isSuccessful) {
    //         val body = response.body()
    //         if (body?.data == null) break
    //         result.addAll(body.data.map { it.manga })
    //         offset += body.data.size
    //         response = malService.getUserMangaList(
    //             username = username ?: "@me",
    //             offset = offset
    //         )
    //         if (body.data.size < 100) break
    //     }
    //     return result.sortedByDescending { it.mean }
    // }

    suspend fun logout() {
        userManager.logout()
    }

    companion object {
        const val MAL_AUTH_REDIRECT_HOST = "grigmal.auth"
    }
}