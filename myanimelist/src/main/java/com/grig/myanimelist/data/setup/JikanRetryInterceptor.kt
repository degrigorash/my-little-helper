package com.grig.myanimelist.data.setup

import okhttp3.Interceptor
import okhttp3.Response
import java.io.InterruptedIOException

/**
 * Retries catalogue (Tenrai/Jikan) requests that fail with transient upstream errors.
 *
 * Both APIs are caches in front of MyAnimeList and can return transient 429/5xx —
 * for Jikan, a cache miss triggers a live scrape that can exceed its gateway
 * timeout (504), and the first failed request usually warms its cache, so a
 * short-delay retry succeeds far more often than not.
 *
 * Sits outside [TenraiFallbackInterceptor], so each retry round re-attempts the
 * whole Tenrai-then-Jikan sequence.
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
