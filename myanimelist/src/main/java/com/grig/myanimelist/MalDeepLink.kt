package com.grig.myanimelist

import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

private val MAL_HOSTS = setOf("myanimelist.net", "www.myanimelist.net")

/**
 * Maps a myanimelist.net web URL to the in-app back stack that shows it, outermost first
 * (e.g. a reviews page opens on top of its anime), or an empty list when the page has no
 * in-app screen. Slugs and query parameters (`?_user=…`) are ignored.
 *
 * Keep in sync with the `myanimelist.net` intent filter in the app manifest.
 */
fun parseMalDeepLink(url: String?): List<MalRoute> {
    val httpUrl = url?.toHttpUrlOrNull() ?: return emptyList()
    if (httpUrl.host !in MAL_HOSTS) return emptyList()

    // /{section}/{id}/{slug}/{subPage}, e.g. /anime/65077/Mushoku_Tensei/reviews
    val segments = httpUrl.pathSegments.filter { it.isNotEmpty() }
    val id = segments.getOrNull(1)?.toIntOrNull()
    val subPage = segments.getOrNull(3)

    return when (segments.firstOrNull()) {
        "anime" -> when {
            // /anime/producer/{id}/{slug} covers studios, producers and licensors alike.
            segments.getOrNull(1) == "producer" ->
                listOfNotNull(segments.getOrNull(2)?.toIntOrNull()?.let { MalRoute.StudioDetail(it) })
            id != null -> mediaRoutes(MalRoute.AnimeDetail(id), id, "anime", subPage)
            else -> emptyList()
        }
        "manga" -> if (id != null) mediaRoutes(MalRoute.MangaDetail(id), id, "manga", subPage) else emptyList()
        "character" -> listOfNotNull(id?.let { MalRoute.CharacterDetail(it) })
        // Voice actors and authors share /people; PersonDetail hands non-voice-actors to AuthorDetail.
        "people" -> listOfNotNull(id?.let { MalRoute.PersonDetail(it) })
        else -> emptyList()
    }
}

private fun mediaRoutes(detail: MalRoute, id: Int, mediaType: String, subPage: String?): List<MalRoute> =
    when (subPage) {
        "reviews" -> listOf(detail, MalRoute.Reviews(id, mediaType))
        "characters" -> listOf(detail, MalRoute.Characters(id, mediaType))
        // stats, pics, episodes, news, … have no dedicated screen, so land on the detail.
        else -> listOf(detail)
    }
