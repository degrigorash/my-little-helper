package com.grig.myanimelist.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.grig.core.theme.AppTheme
import com.grig.myanimelist.R
import com.grig.myanimelist.ui.common.MalErrorContent
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

    MalErrorContent(
        title = title,
        description = description,
        onRetry = onRetry
    )
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
