package com.grig.myanimelist.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grig.core.theme.AppTheme
import com.grig.core.theme.AppThemeExtended
import com.grig.myanimelist.R
import com.grig.myanimelist.ui.home.MalTab
import java.io.IOException

@Composable
fun MalError(
    activeTab: MalTab,
    exception: Throwable?,
    onRetry: () -> Unit
) {
    val isNetworkError = exception?.cause is IOException ||
        exception is IOException

    val title = if (isNetworkError) {
        stringResource(R.string.error_no_internet)
    } else {
        stringResource(R.string.error_something_went_wrong)
    }

    val listName = when (activeTab) {
        MalTab.Anime -> stringResource(R.string.list_name_anime)
        MalTab.Manga -> stringResource(R.string.list_name_manga)
    }
    val description = if (isNetworkError) {
        stringResource(R.string.error_no_internet_description)
    } else {
        stringResource(R.string.error_load_list_description, listName)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(
                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_error_outline),
                contentDescription = null,
                modifier = Modifier.size(56.dp),
                tint = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onRetry,
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AppThemeExtended.colorScheme.malCardStart,
                contentColor = AppThemeExtended.colorScheme.cardText
            )
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_refresh),
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
            Text(text = stringResource(R.string.error_try_again))
        }
    }
}

@Preview(name = "MalError - General")
@Composable
private fun MalErrorPreview() {
    AppTheme(darkTheme = false) {
        MalError(
            activeTab = MalTab.Anime,
            exception = RuntimeException("Something went wrong"),
            onRetry = {}
        )
    }
}

@Preview(name = "MalError - Network Dark")
@Composable
private fun MalErrorNetworkDarkPreview() {
    AppTheme(darkTheme = true) {
        MalError(
            activeTab = MalTab.Manga,
            exception = IOException("No internet"),
            onRetry = {}
        )
    }
}
