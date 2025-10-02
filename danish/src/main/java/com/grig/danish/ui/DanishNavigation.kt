package com.grig.danish.ui

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.grig.danish.ui.theme.DanishTheme

fun NavGraphBuilder.danishNavigation() {
    composable("danish_home") {
        DanishTheme {
            DanishHome()
        }
    }
}