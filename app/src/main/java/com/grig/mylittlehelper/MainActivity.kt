package com.grig.mylittlehelper

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.lifecycleScope
import com.grig.myanimelist.data.MalRepository
import com.grig.myanimelist.data.MalRepository.Companion.MAL_AUTH_REDIRECT_HOST
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var malRepository: MalRepository

    @Inject
    lateinit var dataStore: DataStore<Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        handleMalAuthRedirect(intent.data)
        setContent {
            MyLittleHelperNavHost()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleMalAuthRedirect(intent.data)
    }

    private fun handleMalAuthRedirect(uri: Uri?) {
        if (uri?.host != MAL_AUTH_REDIRECT_HOST) return
        malRepository.getAuthorizationCode(uri)?.let { code ->
            lifecycleScope.launch {
                malRepository.auth(code)
            }
        }
    }
}



