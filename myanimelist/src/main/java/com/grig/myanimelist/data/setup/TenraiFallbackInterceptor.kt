package com.grig.myanimelist.data.setup

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Falls back from Tenrai to Jikan when Tenrai can't serve a request.
 *
 * Tenrai (api.tenrai.org) is the primary catalogue API; it implements the Jikan v4
 * schema, so the same request can be replayed against api.jikan.moe by swapping the
 * host and the version prefix. Falling back covers Tenrai's beta downtime, its
 * anti-abuse 403s, and per-IP rate limiting (429) — Jikan enforces its own,
 * independent limits.
 *
 * Sits inside [JikanRetryInterceptor] in the chain, so each retry round attempts
 * Tenrai first and Jikan second.
 */
class TenraiFallbackInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val primary = try {
            chain.proceed(request)
        } catch (e: IOException) {
            null
        }
        if (primary != null && !shouldFallBack(primary.code)) return primary
        primary?.close()

        val fallbackUrl = request.url.newBuilder()
            .host(FALLBACK_HOST)
            .encodedPath(
                request.url.encodedPath.replaceFirst(PRIMARY_PREFIX, FALLBACK_PREFIX)
            )
            .build()
        return chain.proceed(request.newBuilder().url(fallbackUrl).build())
    }

    // 403: Tenrai anti-abuse false positive; 429: per-IP rate limit; 5xx: outage.
    private fun shouldFallBack(code: Int) = code == 403 || code == 429 || code >= 500

    private companion object {
        const val FALLBACK_HOST = "api.jikan.moe"
        const val PRIMARY_PREFIX = "/v1/"
        const val FALLBACK_PREFIX = "/v4/"
    }
}
