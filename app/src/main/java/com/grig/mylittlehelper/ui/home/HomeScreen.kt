package com.grig.mylittlehelper.ui.home

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun HomeScreen(
    viewModel: HomeViewModel
) {
    val isDarkTheme = !isSystemInDarkTheme()
    val uriHandler = LocalUriHandler.current
    val user = viewModel.userFlow.collectAsState(initial = null).value

    val prettyJson = Json { // this returns the JsonBuilder
        prettyPrint = true
        // optional: specify indent
        prettyPrintIndent = " "
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Button(
            onClick = {
                viewModel.tryLogin { uriHandler.openUri(it) }
            }
        ) {
            Text(text = "Login")
            if (user != null) {
                Text(text = prettyJson.encodeToString(user))
            }
        }
    }
}