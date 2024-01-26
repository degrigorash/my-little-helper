package com.grig.myanimelist.tools

import android.util.Base64
import java.security.SecureRandom

internal fun generateCodeVerifier(): String {
    val secureRandom = SecureRandom()
    val bytes = ByteArray(64)
    secureRandom.nextBytes(bytes)
    val encoding = Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP
    return Base64.encodeToString(bytes, encoding)
}