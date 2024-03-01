package com.grig.mylittlehelper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.grig.myanimelist.data.MalRepository
import com.grig.myanimelist.data.MalRepository.Companion.MAL_AUTH_REDIRECT_HOST
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var malRepository: MalRepository

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

        setContent {
            MyLittleHelperNavHost()
        }
    }
}



