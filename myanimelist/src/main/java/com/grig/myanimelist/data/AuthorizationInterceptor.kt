package com.grig.myanimelist.data

import okhttp3.Interceptor
import okhttp3.Response

internal class AuthorizationInterceptor(
    private val userManager: UserManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()
        if (userManager.accessToken != null) {
            val newReq = req.newBuilder()
                .header("Authorization", "Bearer ${userManager.accessToken}")
                .build()
            return chain.proceed(newReq)
        }
        return chain.proceed(req)
    }
}