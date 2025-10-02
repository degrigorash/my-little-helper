package com.grig.danish.data

import javax.inject.Inject

class DanishRepository @Inject constructor(
    private val service: DanishService
) {

    suspend fun word(word: String) = service.word(
        query = word
    )
}
