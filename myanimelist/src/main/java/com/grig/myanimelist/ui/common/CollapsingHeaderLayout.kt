package com.grig.myanimelist.ui.common

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.grig.core.theme.AppTheme
import com.grig.core.theme.AppThemeExtended
import com.grig.myanimelist.R

internal val IMAGE_MAX_HEIGHT = 300.dp
internal val TOOLBAR_HEIGHT = 64.dp

@Composable
fun CollapsingHeaderLayout(
    title: String,
    imageUrl: String?,
    onBackClick: () -> Unit,
    onImageClick: () -> Unit,
    modifier: Modifier = Modifier,
    imageContentScale: ContentScale = ContentScale.Crop,
    content: @Composable (titleAlpha: Float) -> Unit
) {
    val density = LocalDensity.current
    val scrollState = rememberScrollState()
    val statusBarHeight = WindowInsets.statusBars.getTop(density)

    val imageMaxHeightPx = with(density) { IMAGE_MAX_HEIGHT.toPx() } + statusBarHeight

    val collapseProgress by remember {
        derivedStateOf {
            (scrollState.value / imageMaxHeightPx).coerceIn(0f, 1f)
        }
    }

    val imageHeightPx by remember {
        derivedStateOf {
            (imageMaxHeightPx - scrollState.value).coerceAtLeast(0f)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        ScrollableContent(
            scrollState = scrollState,
            imageMaxHeightPx = imageMaxHeightPx,
            collapseProgress = collapseProgress,
            content = content
        )

        CollapsingImage(
            imageUrl = imageUrl,
            visibleHeightPx = imageHeightPx,
            fullHeightPx = imageMaxHeightPx,
            onImageClick = onImageClick,
            contentScale = imageContentScale
        )

        StatusBarScrim(collapseProgress = collapseProgress)

        ToolbarOverlay(
            title = title,
            collapseProgress = collapseProgress,
            onBackClick = onBackClick
        )
    }
}

@Composable
private fun ScrollableContent(
    scrollState: ScrollState,
    imageMaxHeightPx: Float,
    collapseProgress: Float,
    content: @Composable (titleAlpha: Float) -> Unit
) {
    val density = LocalDensity.current
    val imageSpacerHeight = with(density) { imageMaxHeightPx.toDp() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .navigationBarsPadding()
    ) {
        Spacer(modifier = Modifier.height(imageSpacerHeight))

        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            content(1f - collapseProgress)
        }
    }
}

@Composable
internal fun CollapsingImage(
    imageUrl: String?,
    visibleHeightPx: Float,
    fullHeightPx: Float,
    onImageClick: () -> Unit,
    contentScale: ContentScale = ContentScale.Crop
) {
    if (imageUrl == null || visibleHeightPx <= 0f) return

    val density = LocalDensity.current
    val visibleHeightDp = with(density) { visibleHeightPx.toDp() }
    val fullHeightDp = with(density) { fullHeightPx.toDp() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(visibleHeightDp)
            .clipToBounds()
            .clickable(onClick = onImageClick)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(fullHeightDp),
            contentScale = contentScale,
            alignment = Alignment.TopCenter
        )
    }
}

@Composable
internal fun StatusBarScrim(collapseProgress: Float) {
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val scrimAlpha = (1f - collapseProgress).coerceIn(0f, 1f) * 0.4f
    val scrimColor = if (isSystemInDarkTheme()) Color.Black else Color.White

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(statusBarHeight * 2)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        scrimColor.copy(alpha = scrimAlpha),
                        Color.Transparent
                    )
                )
            )
    )
}

@Composable
internal fun ToolbarOverlay(
    title: String,
    collapseProgress: Float,
    onBackClick: () -> Unit
) {
    val colors = AppThemeExtended.colorScheme

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.malCardStart.copy(alpha = collapseProgress))
            .statusBarsPadding()
            .height(TOOLBAR_HEIGHT)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                val scrimAlpha = (1f - collapseProgress) * 0.5f
                val scrimColor = if (isSystemInDarkTheme()) Color.Black else Color.White
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            color = scrimColor.copy(alpha = scrimAlpha),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back),
                        contentDescription = "Back",
                        tint = colors.headerText
                    )
                }
            }

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = colors.headerText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
                    .graphicsLayer { alpha = collapseProgress }
            )
        }
    }
}

@Preview(name = "Collapsing Header - Light")
@Composable
private fun CollapsingHeaderLayoutLightPreview() {
    AppTheme(darkTheme = false) {
        CollapsingHeaderLayout(
            title = "Fullmetal Alchemist: Brotherhood",
            imageUrl = null,
            onBackClick = {},
            onImageClick = {}
        ) { titleAlpha ->
            Text(
                text = "Fullmetal Alchemist: Brotherhood",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.graphicsLayer { alpha = titleAlpha }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Sample content goes here", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(name = "Collapsing Header - Dark")
@Composable
private fun CollapsingHeaderLayoutDarkPreview() {
    AppTheme(darkTheme = true) {
        CollapsingHeaderLayout(
            title = "Fullmetal Alchemist: Brotherhood",
            imageUrl = null,
            onBackClick = {},
            onImageClick = {}
        ) { titleAlpha ->
            Text(
                text = "Fullmetal Alchemist: Brotherhood",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.graphicsLayer { alpha = titleAlpha }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Sample content goes here", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
