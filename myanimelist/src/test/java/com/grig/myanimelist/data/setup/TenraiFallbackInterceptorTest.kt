package com.grig.myanimelist.data.setup

import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException
import java.util.concurrent.TimeUnit

class TenraiFallbackInterceptorTest {

    private val interceptor = TenraiFallbackInterceptor()

    private val tenraiRequest = Request.Builder()
        .url("https://api.tenrai.org/v1/anime/9756/relations")
        .build()

    @Test
    fun `successful Tenrai response is returned as-is`() {
        val chain = FakeChain(tenraiRequest) { request -> response(request, 200) }

        val result = interceptor.intercept(chain)

        assertEquals(200, result.code)
        assertEquals("api.tenrai.org", result.request.url.host)
        assertEquals(listOf("api.tenrai.org"), chain.requestedHosts)
    }

    @Test
    fun `client errors other than 403 and 429 do not fall back`() {
        val chain = FakeChain(tenraiRequest) { request -> response(request, 404) }

        val result = interceptor.intercept(chain)

        assertEquals(404, result.code)
        assertEquals(listOf("api.tenrai.org"), chain.requestedHosts)
    }

    @Test
    fun `5xx from Tenrai replays the request against Jikan with v4 prefix`() {
        val chain = FakeChain(tenraiRequest) { request ->
            if (request.url.host == "api.tenrai.org") response(request, 503)
            else response(request, 200)
        }

        val result = interceptor.intercept(chain)

        assertEquals(200, result.code)
        assertEquals("api.jikan.moe", result.request.url.host)
        assertEquals("/v4/anime/9756/relations", result.request.url.encodedPath)
        assertEquals(listOf("api.tenrai.org", "api.jikan.moe"), chain.requestedHosts)
    }

    @Test
    fun `rate limit and anti-abuse codes fall back`() {
        for (code in listOf(403, 429)) {
            val chain = FakeChain(tenraiRequest) { request ->
                if (request.url.host == "api.tenrai.org") response(request, code)
                else response(request, 200)
            }

            val result = interceptor.intercept(chain)

            assertEquals("api.jikan.moe", result.request.url.host)
            assertEquals(200, result.code)
        }
    }

    @Test
    fun `IOException from Tenrai falls back to Jikan`() {
        val chain = FakeChain(tenraiRequest) { request ->
            if (request.url.host == "api.tenrai.org") throw IOException("connect timeout")
            else response(request, 200)
        }

        val result = interceptor.intercept(chain)

        assertEquals(200, result.code)
        assertEquals("api.jikan.moe", result.request.url.host)
    }

    @Test
    fun `query parameters survive the fallback rewrite`() {
        val request = Request.Builder()
            .url("https://api.tenrai.org/v1/anime?producers=44&order_by=score&sort=desc&page=1&limit=25")
            .build()
        val chain = FakeChain(request) { req ->
            if (req.url.host == "api.tenrai.org") response(req, 504)
            else response(req, 200)
        }

        val result = interceptor.intercept(chain)

        assertEquals("api.jikan.moe", result.request.url.host)
        assertEquals("/v4/anime", result.request.url.encodedPath)
        assertEquals("producers=44&order_by=score&sort=desc&page=1&limit=25", result.request.url.encodedQuery)
    }

    private fun response(request: Request, code: Int): Response = Response.Builder()
        .request(request)
        .protocol(Protocol.HTTP_1_1)
        .code(code)
        .message("test")
        .build()

    /** Minimal [Interceptor.Chain] that records each proceeded request's host. */
    private class FakeChain(
        private val request: Request,
        private val handler: (Request) -> Response
    ) : Interceptor.Chain {

        val requestedHosts = mutableListOf<String>()

        override fun request(): Request = request

        override fun proceed(request: Request): Response {
            requestedHosts += request.url.host
            return handler(request)
        }

        override fun connection() = null
        override fun call(): Call = throw UnsupportedOperationException()
        override fun connectTimeoutMillis() = 0
        override fun withConnectTimeout(timeout: Int, unit: TimeUnit) = this
        override fun readTimeoutMillis() = 0
        override fun withReadTimeout(timeout: Int, unit: TimeUnit) = this
        override fun writeTimeoutMillis() = 0
        override fun withWriteTimeout(timeout: Int, unit: TimeUnit) = this
    }
}
