package com.grig.myanimelist.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.WindowCompat
import coil.compose.AsyncImage
import com.grig.core.theme.AppTheme
import com.grig.core.theme.AppThemeExtended
import com.grig.myanimelist.R

@Composable
fun FullscreenImageViewer(
    imageUrls: List<String>,
    initialPage: Int = 0,
    onDismiss: () -> Unit
) {
    if (imageUrls.isEmpty()) return

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
    ) {
        val colors = AppThemeExtended.colorScheme
        val pagerState = rememberPagerState(initialPage = initialPage) { imageUrls.size }

        val view = LocalView.current
        SideEffect {
            val window = (view.parent as? DialogWindowProvider)?.window ?: return@SideEffect
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(colors.gradientBackgroundTop, colors.gradientBackgroundBottom)
                    )
                )
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                key = { imageUrls[it] }
            ) { page ->
                ZoomableImage(imageUrl = imageUrls[page])
            }

            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding()
                    .padding(8.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = colors.malCardStart.copy(alpha = 0.5f),
                    contentColor = colors.cardText
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = "Close"
                )
            }

            if (imageUrls.size > 1) {
                Text(
                    text = "${pagerState.currentPage + 1} / ${imageUrls.size}",
                    color = colors.cardText,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .navigationBarsPadding()
                        .padding(bottom = 16.dp)
                        .background(
                            color = colors.malCardStart.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun ZoomableImage(imageUrl: String) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val isZoomed = scale > 1.01f

    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(isZoomed) {
                awaitEachGesture {
                    awaitFirstDown(requireUnconsumed = false)
                    do {
                        val event = awaitPointerEvent()
                        val zoomChange = event.calculateZoom()
                        val panChange = event.calculatePan()

                        if (zoomChange != 1f) {
                            val newScale = (scale * zoomChange).coerceIn(1f, 5f)
                            scale = newScale
                            offset = if (newScale > 1f) offset + panChange else Offset.Zero
                            event.changes.fastForEach { it.consume() }
                        } else if (isZoomed) {
                            offset += panChange
                            event.changes.fastForEach { it.consume() }
                        }
                    } while (event.changes.any { it.pressed })
                }
            }
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationX = offset.x
                translationY = offset.y
            },
        contentScale = ContentScale.Fit
    )
}

@Preview(name = "Fullscreen Image Viewer - Light")
@Composable
private fun FullscreenImageViewerPreview() {
    AppTheme(darkTheme = false) {
        FullscreenImageViewer(
            imageUrls = listOf("https://example.com/image.jpg"),
            onDismiss = {}
        )
    }
}

@Preview(name = "Fullscreen Image Viewer - Dark")
@Composable
private fun FullscreenImageViewerDarkPreview() {
    AppTheme(darkTheme = true) {
        FullscreenImageViewer(
            imageUrls = listOf("https://example.com/image.jpg"),
            onDismiss = {}
        )
    }
}

@Preview(name = "Fullscreen Image Viewer - Gallery Light")
@Composable
private fun FullscreenImageViewerGalleryPreview() {
    AppTheme(darkTheme = false) {
        FullscreenImageViewer(
            imageUrls = listOf(
                "https://example.com/image1.jpg",
                "https://example.com/image2.jpg",
                "https://example.com/image3.jpg"
            ),
            onDismiss = {}
        )
    }
}

@Preview(name = "Fullscreen Image Viewer - Gallery Dark")
@Composable
private fun FullscreenImageViewerGalleryDarkPreview() {
    AppTheme(darkTheme = true) {
        FullscreenImageViewer(
            imageUrls = listOf(
                "https://example.com/image1.jpg",
                "https://example.com/image2.jpg",
                "https://example.com/image3.jpg"
            ),
            onDismiss = {}
        )
    }
}
