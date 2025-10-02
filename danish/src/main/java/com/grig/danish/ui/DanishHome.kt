package com.grig.danish.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.grig.danish.ui.theme.Primary

@Preview(showBackground = true)
@Composable
fun DanishHomePreview() {
    DanishHome()
}

@Composable
fun DanishHome(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(Primary)
    ) {
    }
}