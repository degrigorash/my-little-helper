package com.grig.myanimelist.data.setup

import okhttp3.Interceptor
import okhttp3.Response
import java.io.InterruptedIOException

/**
 * Retries Jikan requests that fail with transient upstream errors.
 *
 * Jikan is a caching scraper in front of MyAnimeList: a cache miss triggers a live
 * scrape that can exceed Jikan's gateway timeout (504) or hit MAL's rate limiting
 * (429/503). The first failed request usually warms Jikan's cache, so a short-delay
 * retry succeeds far more often than not.
 */
class JikanRetryInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var response = chain.proceed(chain.request())
        var attempt = 0
        while (response.code in RETRYABLE_CODES && attempt < MAX_RETRIES) {
            response.close()
            attempt++
            try {
                Thread.sleep(RETRY_DELAY_MS * attempt)
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
                throw InterruptedIOException("Retry interrupted")
            }
            response = chain.proceed(chain.request())
        }
        return response
    }

    private companion object {
        val RETRYABLE_CODES = setOf(429, 502, 503, 504)
        const val MAX_RETRIES = 2
        const val RETRY_DELAY_MS = 1_000L
    }
}
