package com.grig.myanimelist.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grig.core.theme.AppTheme

@Composable
fun MalLoading() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .width(64.dp)
                .align(Alignment.Center)
        )
    }
}

@Preview(name = "MalLoading")
@Composable
private fun MalLoadingPreview() {
    AppTheme(darkTheme = false) {
        MalLoading()
    }
}

@Preview(name = "MalLoading - Dark")
@Composable
private fun MalLoadingDarkPreview() {
    AppTheme(darkTheme = true) {
        MalLoading()
    }
}