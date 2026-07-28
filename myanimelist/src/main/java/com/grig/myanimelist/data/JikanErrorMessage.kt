package com.grig.myanimelist.data

import retrofit2.HttpException
import java.io.IOException

/**
 * Maps catalogue-API (Tenrai/Jikan) failures to a user-facing message. Their 5xx
 * responses (most often 504) mean the API couldn't serve MyAnimeList data — the
 * data source is down, not the user's connection — so the raw "HTTP 504
 * Gateway Time-out" would mislead. A non-2xx surfaces here only after both
 * Tenrai and the Jikan fallback failed.
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
