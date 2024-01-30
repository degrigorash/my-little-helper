package com.grig.mylittlehelper.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import coil.compose.AsyncImage
import com.grig.myanimelist.data.model.MalUserState

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navigateToAnimeList: () -> Unit
) {
    val isDarkTheme = !isSystemInDarkTheme()
    val uriHandler = LocalUriHandler.current
    val userState = viewModel.userFlow.collectAsState(initial = MalUserState.Unauthorized).value

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Button(
            onClick = {
                viewModel.tryLogin { uriHandler.openUri(it) }
            },
            enabled = userState is MalUserState.Unauthorized
        ) {
            Text(text = "Login")
        }
        if(userState is MalUserState.Authorized) {
            Row(
                modifier = Modifier
                    .clickable {
                        navigateToAnimeList()
                    }
            ) {
                AsyncImage(
                    model = userState.user.picture,
                    contentDescription = null,
                )
                Column {
                    Text(text = userState.user.name)
                    Text(text = userState.user.animeStatistics.toString())
                }
            }
        }
    }
}