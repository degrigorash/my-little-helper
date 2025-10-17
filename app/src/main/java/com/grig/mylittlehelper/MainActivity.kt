package com.grig.mylittlehelper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.grig.myanimelist.data.MalRepository
import com.grig.myanimelist.data.MalRepository.Companion.MAL_AUTH_REDIRECT_HOST
import com.grig.mylittlehelper.di.DataStoreModule
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var malRepository: MalRepository

    @Inject
    lateinit var dataStore: DataStore<Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.data?.let {
            when (it.host) {
                MAL_AUTH_REDIRECT_HOST -> {
                    malRepository.getAuthorizationCode(it)?.let { code ->
                        lifecycleScope.launch {
                            malRepository.auth(code)
                            intent.data = null
                        }
                    }
                }

                else -> {}
            }
        }

        val startScreen: String? = runBlocking {
            dataStore.data.map {
                it[
                    stringPreferencesKey(
                        DataStoreModule.DataStoreKeys.START_SCREEN.value
                    )
                ]
            }.firstOrNull()
        }

        setContent {
            MyLittleHelperNavHost(
                startDestination = startScreen ?: "home",
                saveStartScreen = { screen ->
                    runBlocking {
                        dataStore.edit {
                            it[
                                stringPreferencesKey(
                                    DataStoreModule.DataStoreKeys.START_SCREEN.value
                                )
                            ] = screen
                        }
                    }
                }
            )
        }
    }
}



