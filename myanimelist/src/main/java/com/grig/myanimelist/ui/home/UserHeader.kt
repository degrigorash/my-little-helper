package com.grig.myanimelist.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
    onLogoutClick: () -> Unit,
    onLoginClick: () -> Unit
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
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
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
        IconButton(onClick = { if (authorized) onLogoutClick() else onLoginClick() }) {
            Icon(
                painter = painterResource(R.drawable.ic_logout),
                contentDescription = if (authorized) "Logout" else "Login",
                tint = colors.cardText
            )
        }
    }
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
            onLogoutClick = {},
            onLoginClick = {}
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
            onLogoutClick = {},
            onLoginClick = {}
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
            onLogoutClick = {},
            onLoginClick = {}
        )
    }
}
