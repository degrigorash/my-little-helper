package com.grig.danish

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.grig.core.theme.AppTheme
import com.grig.danish.ui.DanishHome

fun NavGraphBuilder.danishNavigation() {
    composable<DanishRoute.DanishHome> {
        AppTheme { DanishHome() }
    }
}