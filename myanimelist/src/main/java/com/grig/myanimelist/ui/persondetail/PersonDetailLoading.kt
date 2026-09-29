package com.grig.myanimelist.ui.persondetail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grig.core.theme.AppTheme
import com.grig.myanimelist.R
import kotlin.math.roundToInt

/**
 * Loading indicator for the person screen. When [progress] is known (0f..1f) it shows a
 * determinate ring that fills as character-favorites lookups complete, with a percentage
 * label; otherwise it falls back to an indeterminate spinner. While [isThrottled], a note
 * below the ring explains that MAL is limiting requests and loading will resume on its own.
 */
@Composable
fun PersonDetailLoading(
    progress: Float?,
    modifier: Modifier = Modifier,
    isThrottled: Boolean = false
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(contentAlignment = Alignment.Center) {
                if (progress != null) {
                    val animatedProgress by animateFloatAsState(
                        targetValue = progress,
                        label = "favoritesProgress"
                    )
                    CircularProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier.size(56.dp)
                    )
                    Text(
                        text = "${(animatedProgress * 100).roundToInt()}%",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                } else {
                    CircularProgressIndicator(modifier = Modifier.size(56.dp))
                }
            }
            AnimatedVisibility(
                visible = isThrottled,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Text(
                    text = stringResource(R.string.person_loading_throttled),
                    modifier = Modifier.padding(start = 32.dp, top = 24.dp, end = 32.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(name = "Person Loading - 0%")
@Composable
private fun PersonDetailLoadingZeroPreview() {
    AppTheme(darkTheme = false) {
        PersonDetailLoading(progress = 0f)
    }
}

@Preview(name = "Person Loading - 60%")
@Composable
private fun PersonDetailLoadingProgressPreview() {
    AppTheme(darkTheme = false) {
        PersonDetailLoading(progress = 0.6f)
    }
}

@Preview(name = "Person Loading - Indeterminate")
@Composable
private fun PersonDetailLoadingIndeterminatePreview() {
    AppTheme(darkTheme = false) {
        PersonDetailLoading(progress = null)
    }
}

@Preview(name = "Person Loading - 60% Dark")
@Composable
private fun PersonDetailLoadingProgressDarkPreview() {
    AppTheme(darkTheme = true) {
        PersonDetailLoading(progress = 0.6f)
    }
}

@Preview(name = "Person Loading - Throttled")
@Composable
private fun PersonDetailLoadingThrottledPreview() {
    AppTheme(darkTheme = false) {
        PersonDetailLoading(progress = 0.7f, isThrottled = true)
    }
}

@Preview(name = "Person Loading - Throttled Dark")
@Composable
private fun PersonDetailLoadingThrottledDarkPreview() {
    AppTheme(darkTheme = true) {
        PersonDetailLoading(progress = 0.7f, isThrottled = true)
    }
}
