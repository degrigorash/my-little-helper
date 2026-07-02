package com.grig.myanimelist.data

import android.net.Uri
import com.grig.myanimelist.clientApiId
import com.grig.myanimelist.data.model.MalUserState
import com.grig.myanimelist.data.model.anime.MalAnime
import com.grig.myanimelist.data.model.jikan.PersonDetail
import com.grig.myanimelist.data.model.jikan.ResolvedRelation
import com.grig.myanimelist.data.model.jikan.VoicedCharacter
import com.grig.myanimelist.data.model.jikan.VoicedCharacterAnime
import com.grig.myanimelist.data.model.manga.MalManga
import com.grig.myanimelist.tools.generateCodeVerifier
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import java.util.concurrent.atomic.AtomicInteger
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MalRepository @Inject constructor(
    private val malAuthService: MalAuthService,
    private val malService: MalService,
    private val jikanService: JikanService,
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
        finishDate: String? = null,
        comments: String? = null
    ) = malService.updateAnimeListStatus(
        animeId = animeId,
        status = status,
        score = score,
        numWatchedEpisodes = numWatchedEpisodes,
        finishDate = finishDate,
        comments = comments
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

    suspend fun getAnimeReviews(animeId: Int, page: Int = 1) =
        jikanService.getAnimeReviews(animeId, page)

    suspend fun getMangaReviews(mangaId: Int, page: Int = 1) =
        jikanService.getMangaReviews(mangaId, page)

    suspend fun getAnimeCharacters(animeId: Int) =
        jikanService.getAnimeCharacters(animeId)

    suspend fun getMangaCharacters(mangaId: Int) =
        jikanService.getMangaCharacters(mangaId)

    suspend fun getCharacterFull(characterId: Int) =
        jikanService.getCharacterFull(characterId)

    suspend fun getProducer(producerId: Int) =
        jikanService.getProducer(producerId)

    suspend fun getAnimeByProducer(producerId: Int, page: Int = 1) =
        jikanService.getAnimeByProducer(producerId, page = page)

    suspend fun getPersonFull(personId: Int) =
        jikanService.getPersonFull(personId)

    /**
     * Fetches a person's profile and voice roles, grouping roles by character (each
     * character carries every anime they appear in), enriching each character with its
     * MAL favorites count, and sorting characters by favorites descending. Fails only if
     * the profile can't be loaded; missing voice roles are tolerated as an empty list.
     *
     * The Jikan voices data has no per-character favorites, so favorites are fetched one
     * request per unique character from the MAL v2 API (ids match Jikan's). These run
     * concurrently but capped at [FAVORITES_FETCH_CONCURRENCY] in flight so a prolific
     * seiyuu (100+ characters) doesn't flood the shared HTTP client at once.
     *
     * [onFavoritesProgress] is invoked (done, total) after each favorites lookup completes,
     * letting the UI show determinate loading progress. It is not called when there are no
     * characters to fetch.
     */
    suspend fun getPersonWithVoices(
        personId: Int,
        onFavoritesProgress: (done: Int, total: Int) -> Unit = { _, _ -> }
    ): Result<PersonDetail> = coroutineScope {
        val person = jikanService.getPersonFull(personId)
            .getOrElse { return@coroutineScope Result.failure(it) }
            .data

        // /people/{id}/full already embeds the full voices array, so no separate call needed.
        val roles = person.voices.filter { it.anime.title.isNotBlank() }

        // Group every role by the character voiced, preserving first-seen character metadata.
        val grouped = roles.groupBy { it.character.malId }

        // Fetch favorites per unique character, bounding concurrency with a semaphore and
        // reporting progress as each lookup finishes.
        val total = grouped.size
        val done = AtomicInteger(0)
        val semaphore = Semaphore(FAVORITES_FETCH_CONCURRENCY)
        val favorites = grouped.keys.map { characterId ->
            async {
                val favs = semaphore.withPermit {
                    malService.getCharacter(characterId).getOrNull()?.numFavorites ?: 0
                }
                onFavoritesProgress(done.incrementAndGet(), total)
                characterId to favs
            }
        }.awaitAll().toMap()

        val characters = grouped.values.map { characterRoles ->
            val first = characterRoles.first()
            VoicedCharacter(
                character = first.character,
                favorites = favorites[first.character.malId] ?: 0,
                roles = characterRoles
                    .map { VoicedCharacterAnime(anime = it.anime, role = it.role) }
                    .sortedByDescending { it.role.equals("Main", ignoreCase = true) }
            )
        }.sortedByDescending { it.favorites }

        Result.success(PersonDetail(person = person, characters = characters))
    }

    suspend fun getAnimeRelatedManga(animeId: Int): List<ResolvedRelation> {
        val rawRelations = jikanService.getAnimeRelations(animeId).getOrNull()
            ?.data
            ?.flatMap { group -> group.entry.map { group.relation to it } }
            ?.filter { (_, entry) -> entry.type == "manga" }
            ?: return emptyList()

        return resolveDetails(rawRelations, isManga = true)
    }

    suspend fun getMangaRelatedAnime(mangaId: Int): List<ResolvedRelation> {
        val rawRelations = jikanService.getMangaRelations(mangaId).getOrNull()
            ?.data
            ?.flatMap { group -> group.entry.map { group.relation to it } }
            ?.filter { (_, entry) -> entry.type == "anime" }
            ?: return emptyList()

        return resolveDetails(rawRelations, isManga = false)
    }

    private suspend fun resolveDetails(
        relations: List<Pair<String, com.grig.myanimelist.data.model.jikan.JikanRelationEntry>>,
        isManga: Boolean
    ): List<ResolvedRelation> = coroutineScope {
        val fields = "main_picture,start_date,media_type,status"
        relations.map { (relation, entry) ->
            async {
                val detail = try {
                    if (isManga) {
                        malService.getMangaDetails(entry.malId, fields = fields).getOrNull()
                    } else {
                        malService.getAnimeDetails(entry.malId, fields = fields).getOrNull()
                    }
                } catch (_: Exception) {
                    null
                }

                val imageUrl = when (detail) {
                    is MalAnime -> detail.pictures?.medium ?: detail.pictures?.large
                    is MalManga -> detail.pictures?.medium ?: detail.pictures?.large
                    else -> null
                }
                val year = when (detail) {
                    is MalAnime -> detail.startDate?.take(4)
                    is MalManga -> detail.startDate?.take(4)
                    else -> null
                }
                val mediaTypeLabel = when (detail) {
                    is MalAnime -> detail.mediaType.displayName
                    is MalManga -> detail.mediaType.displayName
                    else -> null
                }
                val statusLabel = when (detail) {
                    is MalAnime -> detail.status.displayName
                    is MalManga -> detail.status.displayName
                    else -> null
                }

                ResolvedRelation(
                    malId = entry.malId,
                    name = entry.name,
                    type = entry.type,
                    relation = relation,
                    imageUrl = imageUrl,
                    year = year,
                    mediaTypeLabel = mediaTypeLabel,
                    statusLabel = statusLabel
                )
            }
        }.awaitAll()
    }

    suspend fun logout() {
        userManager.logout()
    }

    companion object {
        const val MAL_AUTH_REDIRECT_HOST = "grigmal.auth"

        // Max concurrent MAL character-favorites lookups on the person screen.
        private const val FAVORITES_FETCH_CONCURRENCY = 8
    }
}