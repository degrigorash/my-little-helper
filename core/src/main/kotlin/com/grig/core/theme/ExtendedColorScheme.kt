package com.grig.core.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class ExtendedColorScheme(
    val gradientBackgroundTop: Color,
    val gradientBackgroundBottom: Color,
    val headerText: Color,
    val cardText: Color,
    val malCardStart: Color,
    val malCardEnd: Color,
    val malIconContainer: Color,
    val danishCardStart: Color,
    val danishCardEnd: Color,
    val danishIconContainer: Color,
    val malLoginGradientStart: Color,
    val malLoginGradientEnd: Color,
    val malLoginOutline: Color
)

val LightExtendedColorScheme = ExtendedColorScheme(
    gradientBackgroundTop = gradientBackgroundTopLight,
    gradientBackgroundBottom = gradientBackgroundBottomLight,
    headerText = headerTextLight,
    cardText = cardTextLight,
    malCardStart = malCardStartLight,
    malCardEnd = malCardEndLight,
    malIconContainer = malIconContainerLight,
    danishCardStart = danishCardStartLight,
    danishCardEnd = danishCardEndLight,
    danishIconContainer = danishIconContainerLight,
    malLoginGradientStart = malLoginGradientStartLight,
    malLoginGradientEnd = malLoginGradientEndLight,
    malLoginOutline = malLoginOutlineLight
)

val DarkExtendedColorScheme = ExtendedColorScheme(
    gradientBackgroundTop = gradientBackgroundTopDark,
    gradientBackgroundBottom = gradientBackgroundBottomDark,
    headerText = headerTextDark,
    cardText = cardTextDark,
    malCardStart = malCardStartDark,
    malCardEnd = malCardEndDark,
    malIconContainer = malIconContainerDark,
    danishCardStart = danishCardStartDark,
    danishCardEnd = danishCardEndDark,
    danishIconContainer = danishIconContainerDark,
    malLoginGradientStart = malLoginGradientStartDark,
    malLoginGradientEnd = malLoginGradientEndDark,
    malLoginOutline = malLoginOutlineDark
)

val LocalExtendedColorScheme = staticCompositionLocalOf { LightExtendedColorScheme }
