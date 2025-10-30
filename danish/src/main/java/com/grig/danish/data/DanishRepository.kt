package com.grig.danish.data

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

class DanishRepository @Inject constructor(
    private val service: DanishService,
    @param:ApplicationContext private val appContext: Context
) {

    suspend fun word(word: String): Result<String> {
        val audioDir = ensureAudioDir()
        val target = File(audioDir, "$word.mp3")
        if (target.exists()) {
            return Result.success(Uri.fromFile(target).toString())
        }
        return service.word(
            query = word
        ).map { code ->
            val link = findLink(code)
            val saved = downloadToFile(link, target)
            if (saved != null && saved.exists()) {
                Uri.fromFile(saved).toString()
            } else {
                throw FileNotFoundException()
            }
        }
    }

    private fun ensureAudioDir(): File {
        val dir = File(appContext.filesDir, "danish_audio")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    private suspend fun downloadToFile(url: String, target: File): File? = withContext(Dispatchers.IO) {
        try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.connectTimeout = 10000
            connection.readTimeout = 15000
            connection.requestMethod = "GET"
            connection.instanceFollowRedirects = true
            connection.connect()

            if (connection.responseCode in 200..299) {
                connection.inputStream.use { input ->
                    FileOutputStream(target).use { output ->
                        input.copyTo(output)
                    }
                }
                target
            } else {
                null
            }
        } catch (_: Exception) {
            runCatching { if (target.exists()) target.delete() }
            null
        }
    }

    private fun findLink(code: String): String {
        val linkPart = code
            .substringBefore(".mp3")
            .substringAfter("audio id")
            .substringAfter("https://")
        return "https://${linkPart}.mp3"
    }
}
