package com.grig.myanimelist.data

import okhttp3.Interceptor
import okhttp3.Response

internal class AuthorizationInterceptor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()
        if (tokenManager.accessToken != null) {
            val newReq = req.newBuilder()
                .header("Authorization", "Bearer ${tokenManager.accessToken}")
                .build()
            return chain.proceed(newReq)
        }
        return chain.proceed(req)
    }
}