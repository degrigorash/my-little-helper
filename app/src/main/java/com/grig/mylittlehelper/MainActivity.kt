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
import com.grig.myanimelist.MalRoute
import com.grig.myanimelist.data.MalRepository
import com.grig.myanimelist.data.MalRepository.Companion.MAL_AUTH_REDIRECT_HOST
import com.grig.myanimelist.parseMalDeepLink
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var malRepository: MalRepository

    @Inject
    lateinit var dataStore: DataStore<Preferences>

    private val deepLinks = Channel<List<MalRoute>>(Channel.UNLIMITED)

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        handleMalAuthRedirect(intent.data)
        val launchDeepLink = parseMalDeepLink(intent.dataString)
        // On recreation the NavController restores its own back stack, so replay the link only once.
        if (savedInstanceState == null && launchDeepLink.isNotEmpty()) {
            deepLinks.trySend(launchDeepLink)
        }
        setContent {
            MyLittleHelperNavHost(
                // Home works both signed in and as a guest, so a link doesn't need the login screen first.
                startDestination = if (launchDeepLink.isEmpty()) MalRoute.MalLogin else MalRoute.MalHome,
                deepLinks = deepLinks.receiveAsFlow()
            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleMalAuthRedirect(intent.data)
        parseMalDeepLink(intent.dataString)
            .takeIf { it.isNotEmpty() }
            ?.let { deepLinks.trySend(it) }
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



