package com.grig.myanimelist.data.setup

import com.grig.myanimelist.clientApiId
import com.grig.myanimelist.data.UserManager
import okhttp3.Interceptor
import okhttp3.Response

internal class AuthorizationInterceptor(
    private val userManager: UserManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()
        return if (userManager.accessToken != null) {
            val newReq = req.newBuilder()
                .header("Authorization", "Bearer ${userManager.accessToken}")
                .build()
            chain.proceed(newReq)
        } else {
            val newReq = req.newBuilder()
                .header("X-MAL-CLIENT-ID", clientApiId)
                .build()
            chain.proceed(newReq)
        }
    }
}