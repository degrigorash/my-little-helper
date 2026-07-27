package com.grig.myanimelist.data

import retrofit2.HttpException
import java.io.IOException

/**
 * Maps Jikan failures to a user-facing message. Jikan's 5xx responses (most often
 * 504) mean its scraper couldn't reach MyAnimeList — the data source is down, not
 * the user's connection — so the raw "HTTP 504 Gateway Time-out" would mislead.
 */
fun Throwable.toJikanErrorMessage(fallback: String): String {
    val httpCode = (this as? HttpException)?.code()
    return when {
        httpCode == 429 ->
            "MyAnimeList is busy right now. Please try again in a moment."
        httpCode != null && httpCode >= 500 ->
            "MyAnimeList data is temporarily unavailable. Please try again later."
        this is IOException || cause is IOException ->
            "No internet connection. Check your network and try again."
        else -> fallback
    }
}
