package com.grig.myanimelist.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.grig.core.theme.AppTheme
import com.grig.core.theme.AppThemeExtended
import com.grig.myanimelist.R
import com.grig.myanimelist.data.model.MalUser

@Composable
fun UserHeader(
    user: MalUser?,
    authorized: Boolean,
    guestUsername: String,
    onGuestUsernameChange: (String) -> Unit,
    onGuestSearch: () -> Unit,
    onSearchClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val colors = AppThemeExtended.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    listOf(colors.malCardStart, colors.malCardEnd)
                )
            )
            .statusBarsPadding()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (authorized) {
            AuthorizedContent(user = user)
        } else {
            GuestContent(
                guestUsername = guestUsername,
                onGuestUsernameChange = onGuestUsernameChange,
                onGuestSearch = onGuestSearch
            )
        }
        IconButton(onClick = onSearchClick) {
            Icon(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = "Search",
                tint = colors.cardText
            )
        }
        IconButton(onClick = onLogoutClick) {
            Icon(
                painter = painterResource(R.drawable.ic_logout),
                contentDescription = "Logout",
                tint = colors.cardText
            )
        }
    }
}

@Composable
private fun RowScope.AuthorizedContent(user: MalUser?) {
    val colors = AppThemeExtended.colorScheme
    UserAvatar(user = user)
    Spacer(modifier = Modifier.width(12.dp))
    Column(modifier = Modifier.weight(1f)) {
        Text(
            text = user?.name ?: "Guest",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = colors.cardText,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = if (user?.isSupporter == true) "Premium Member" else "Member",
            style = MaterialTheme.typography.bodyMedium,
            color = colors.cardText.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun RowScope.GuestContent(
    guestUsername: String,
    onGuestUsernameChange: (String) -> Unit,
    onGuestSearch: () -> Unit
) {
    val colors = AppThemeExtended.colorScheme
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = guestUsername,
        onValueChange = onGuestUsernameChange,
        modifier = Modifier
            .weight(1f)
            .height(48.dp),
        placeholder = {
            Text(
                text = "MAL username",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.cardText.copy(alpha = 0.5f)
            )
        },
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            color = colors.cardText,
            fontWeight = FontWeight.SemiBold
        ),
        singleLine = true,
        shape = RoundedCornerShape(24.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colors.cardText,
            unfocusedBorderColor = colors.cardText.copy(alpha = 0.5f),
            cursorColor = colors.cardText
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            keyboardController?.hide()
            onGuestSearch()
        }),
        trailingIcon = {
            IconButton(onClick = {
                keyboardController?.hide()
                onGuestSearch()
            }) {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = "Search",
                    tint = colors.cardText
                )
            }
        }
    )
}

@Composable
private fun UserAvatar(user: MalUser?) {
    if (user?.picture != null) {
        AsyncImage(
            model = user.picture,
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    } else {
        val colors = AppThemeExtended.colorScheme
        Surface(
            modifier = Modifier.size(48.dp),
            shape = CircleShape,
            color = colors.cardText.copy(alpha = 0.2f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = user?.name
                        ?.split(" ")
                        ?.take(2)
                        ?.mapNotNull { it.firstOrNull()?.uppercase() }
                        ?.joinToString("")
                        ?: "?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colors.cardText
                )
            }
        }
    }
}

@Preview(name = "Authorized")
@Composable
private fun UserHeaderAuthorizedPreview() {
    AppTheme(darkTheme = false) {
        UserHeader(
            user = previewUser,
            authorized = true,
            guestUsername = "",
            onGuestUsernameChange = {},
            onGuestSearch = {},
            onSearchClick = {},
            onLogoutClick = {}
        )
    }
}

@Preview(name = "Guest")
@Composable
private fun UserHeaderGuestPreview() {
    AppTheme(darkTheme = false) {
        UserHeader(
            user = null,
            authorized = false,
            guestUsername = "",
            onGuestUsernameChange = {},
            onGuestSearch = {},
            onSearchClick = {},
            onLogoutClick = {}
        )
    }
}

@Preview(name = "Guest with username")
@Composable
private fun UserHeaderGuestWithUsernamePreview() {
    AppTheme(darkTheme = false) {
        UserHeader(
            user = null,
            authorized = false,
            guestUsername = "Naruto_fan42",
            onGuestUsernameChange = {},
            onGuestSearch = {},
            onSearchClick = {},
            onLogoutClick = {}
        )
    }
}

@Preview(name = "Guest Dark")
@Composable
private fun UserHeaderGuestDarkPreview() {
    AppTheme(darkTheme = true) {
        UserHeader(
            user = null,
            authorized = false,
            guestUsername = "Naruto_fan42",
            onGuestUsernameChange = {},
            onGuestSearch = {},
            onSearchClick = {},
            onLogoutClick = {}
        )
    }
}

@Preview(name = "Dark")
@Composable
private fun UserHeaderDarkPreview() {
    AppTheme(darkTheme = true) {
        UserHeader(
            user = previewUser,
            authorized = true,
            guestUsername = "",
            onGuestUsernameChange = {},
            onGuestSearch = {},
            onSearchClick = {},
            onLogoutClick = {}
        )
    }
}
