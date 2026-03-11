package com.grig.mylittlehelper.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grig.core.theme.AppTheme
import com.grig.core.theme.AppThemeExtended
import com.grig.core.theme.ExtendedColorScheme
import com.grig.mylittlehelper.R

@Composable
fun HomeScreen(
    navigateToMal: () -> Unit,
    navigateToDanish: () -> Unit
) {
    val colors = AppThemeExtended.colorScheme

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(colors.gradientBackgroundTop, colors.gradientBackgroundBottom)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 24.dp)
                .padding(top = 32.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = stringResource(R.string.choose_your_path),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = colors.headerText,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.padding(top = 8.dp))

            PathCard(
                title = stringResource(R.string.mal_button),
                description = stringResource(R.string.mal_description),
                gradientStart = colors.malCardStart,
                gradientEnd = colors.malCardEnd,
                iconContainerColor = colors.malIconContainer,
                textColor = colors.cardText,
                iconContent = {
                    Image(
                        painter = painterResource(R.drawable.ic_book),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                },
                onClick = navigateToMal
            )

            PathCard(
                title = stringResource(R.string.learn_danish),
                description = stringResource(R.string.danish_description),
                gradientStart = colors.danishCardStart,
                gradientEnd = colors.danishCardEnd,
                iconContainerColor = colors.danishIconContainer,
                textColor = colors.cardText,
                iconContent = {
                    DanishIcon(colors)
                },
                onClick = navigateToDanish
            )
        }
    }
}

@Composable
private fun DanishIcon(colors: ExtendedColorScheme) {
    Text(
        text = "DA",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = colors.cardText
    )
}

@Composable
private fun PathCard(
    title: String,
    description: String,
    gradientStart: Color,
    gradientEnd: Color,
    iconContainerColor: Color,
    textColor: Color,
    iconContent: @Composable () -> Unit,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(24.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(gradientStart, gradientEnd)
                )
            )
            .clickable(onClick = onClick)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(iconContainerColor),
                contentAlignment = Alignment.Center
            ) {
                iconContent()
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = textColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(name = "Light")
@Composable
fun HomeScreenPreview() {
    AppTheme(darkTheme = false) {
        HomeScreen(
            navigateToMal = {},
            navigateToDanish = {}
        )
    }
}

@Preview(name = "Dark")
@Composable
fun HomeScreenDarkPreview() {
    AppTheme(darkTheme = true) {
        HomeScreen(
            navigateToMal = {},
            navigateToDanish = {}
        )
    }
}
