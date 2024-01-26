package com.grig.myanimelist.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Named

class TokenManager @Inject constructor(
    @Named("AuthDataStore")
    private val dataStore: DataStore<Preferences>
) {

    val accessTokenFlow = dataStore.data.map { it[ACCESS_TOKEN] }
    val accessToken = runBlocking { accessTokenFlow.first() }

    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        dataStore.edit { tokenData ->
            tokenData[ACCESS_TOKEN] = accessToken
            tokenData[REFRESH_TOKEN] = refreshToken
        }
    }

    companion object {
        val ACCESS_TOKEN = stringPreferencesKey("accessToken")
        val REFRESH_TOKEN = stringPreferencesKey("refreshToken")
    }
}