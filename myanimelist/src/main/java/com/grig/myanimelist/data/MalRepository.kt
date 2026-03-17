package com.grig.myanimelist.data

import android.net.Uri
import com.grig.myanimelist.clientApiId
import com.grig.myanimelist.data.model.MalUserState
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

    suspend fun searchAnime(query: String) =
        malService.searchAnime(query)

    suspend fun getAnimeDetails(animeId: Int) =
        malService.getAnimeDetails(animeId)

    suspend fun searchManga(query: String) =
        malService.searchManga(query)

    suspend fun getMangaDetails(mangaId: Int) =
        malService.getMangaDetails(mangaId)

    suspend fun getUserAnimeList(
        username: String?,
        offset: Int,
        status: String? = null
    ) = malService.getUserAnimeList(
        username = username ?: "@me",
        offset = offset,
        status = status
    )

    suspend fun updateAnimeListStatus(
        animeId: Int,
        status: String? = null,
        score: Int? = null,
        numWatchedEpisodes: Int? = null,
        finishDate: String? = null
    ) = malService.updateAnimeListStatus(
        animeId = animeId,
        status = status,
        score = score,
        numWatchedEpisodes = numWatchedEpisodes,
        finishDate = finishDate
    )

    suspend fun deleteAnimeListItem(animeId: Int) =
        malService.deleteAnimeListItem(animeId)

    suspend fun getUserMangaList(
        username: String?,
        offset: Int,
        status: String? = null
    ) = malService.getUserMangaList(
        username = username ?: "@me",
        offset = offset,
        status = status
    )

    suspend fun updateMangaListStatus(
        mangaId: Int,
        status: String? = null,
        score: Int? = null,
        numChaptersRead: Int? = null,
        numVolumesRead: Int? = null,
        finishDate: String? = null
    ) = malService.updateMangaListStatus(
        mangaId = mangaId,
        status = status,
        score = score,
        numChaptersRead = numChaptersRead,
        numVolumesRead = numVolumesRead,
        finishDate = finishDate
    )

    suspend fun deleteMangaListItem(mangaId: Int) =
        malService.deleteMangaListItem(mangaId)

    suspend fun logout() {
        userManager.logout()
    }

    companion object {
        const val MAL_AUTH_REDIRECT_HOST = "grigmal.auth"
    }
}