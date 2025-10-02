package com.grig.myanimelist.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.grig.myanimelist.data.model.MalUser
import com.grig.myanimelist.data.model.MalUserState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Named

class UserManager @Inject constructor(
    @param:Named("AuthDataStore")
    private val dataStore: DataStore<Preferences>
) {

    val accessToken: String?
        get() = runBlocking { dataStore.data.map { it[ACCESS_TOKEN] }.first() }
    val userFlow = dataStore.data.map {
        it[USER]?.let { user ->
            MalUserState.Authorized(Json.decodeFromString<MalUser>(user))
        } ?: MalUserState.Unauthorized
    }

    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        dataStore.edit { tokenData ->
            tokenData[ACCESS_TOKEN] = accessToken
            tokenData[REFRESH_TOKEN] = refreshToken
        }
    }

    suspend fun saveUser(user: MalUser) {
        dataStore.edit { userData ->
            userData[USER] = Json.encodeToString(user)
        }
    }

    suspend fun logout() {
        dataStore.edit { userData ->
            userData.remove(ACCESS_TOKEN)
            userData.remove(REFRESH_TOKEN)
            userData.remove(USER)
        }
    }

    companion object {
        val ACCESS_TOKEN = stringPreferencesKey("accessToken")
        val REFRESH_TOKEN = stringPreferencesKey("refreshToken")
        val USER = stringPreferencesKey("user")
    }
}