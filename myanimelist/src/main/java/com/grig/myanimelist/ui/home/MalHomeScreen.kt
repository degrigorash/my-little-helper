package com.grig.myanimelist.ui.home

import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.grig.myanimelist.R
import com.grig.myanimelist.data.model.MalUserState

@Composable
fun MalHomeScreen(
    modifier: Modifier = Modifier,
    viewModel: MalHomeViewModel,
    navigateToAnimeList: (username: String?) -> Unit,
    navigateToMangaList: (username: String?) -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()
    val uriHandler = LocalUriHandler.current
    val userState = viewModel.malUserFlow.collectAsState(initial = MalUserState.Unauthorized).value
    val authorized = userState is MalUserState.Authorized
    val user = (userState as? MalUserState.Authorized)?.user
    var checked by rememberSaveable { mutableStateOf(false) }
    var customUsername by rememberSaveable { mutableStateOf("") }
    val buttonsEnabled = authorized || customUsername.isNotBlank()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            AsyncImage(
                modifier = Modifier
                    .width(120.dp)
                    .height(120.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .border(
                        width = 2.dp,
//                        color = if (isDarkTheme) Primary50 else Primary900,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(6.dp)
                    ),
                model = user?.picture,
                contentDescription = null,
                placeholder = painterResource(R.drawable.mal_user_placeholder),
                fallback = painterResource(R.drawable.mal_user_placeholder),
                error = painterResource(R.drawable.mal_user_placeholder)
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {
                if (user != null) {
                    Text(
                        text = user.name,
                        style = if (user.name.length <= 16) {
                            MaterialTheme.typography.headlineMedium
                        } else {
                            MaterialTheme.typography.headlineSmall
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Button(
                    modifier = Modifier
                        .padding(top = 8.dp),
                    onClick = {
                        if (authorized) {
                            viewModel.malLogout()
                        } else {
                            viewModel.malLogin { uriHandler.openUri(it) }
                        }
                    }
                ) {
                    Text(
                        text = stringResource(if (authorized) R.string.logout else R.string.login)
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 16.dp)
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(end = 8.dp),
                onClick = {
                    navigateToAnimeList(
                        if (checked && customUsername.isNotBlank()) {
                            customUsername
                        } else {
                            user?.name
                        }
                    )
                },
                enabled = buttonsEnabled
            ) {
                Text(
                    text = stringResource(R.string.anime_list)
                )
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp),
                onClick = {
                    navigateToMangaList(
                        if (checked && customUsername.isNotBlank()) {
                            customUsername
                        } else {
                            user?.name
                        }
                    )
                },
                enabled = buttonsEnabled
            ) {
                Text(
                    text = stringResource(R.string.manga_list)
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Switch(
                checked = checked,
                onCheckedChange = {
                    checked = it
                    if (!it) customUsername = ""
                },
            )
            Text(
                modifier = Modifier
                    .padding(start = 8.dp),
                text = stringResource(R.string.search_for_user),
                style = MaterialTheme.typography.titleLarge
            )
        }
        if (checked) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                value = customUsername,
                onValueChange = { customUsername = it }
            )
        }
    }
}