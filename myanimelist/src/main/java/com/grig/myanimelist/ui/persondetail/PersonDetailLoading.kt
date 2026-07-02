package com.grig.myanimelist.ui.persondetail

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grig.core.theme.AppTheme
import kotlin.math.roundToInt

/**
 * Loading indicator for the person screen. When [progress] is known (0f..1f) it shows a
 * determinate ring that fills as character-favorites lookups complete, with a percentage
 * label; otherwise it falls back to an indeterminate spinner.
 */
@Composable
fun PersonDetailLoading(
    progress: Float?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
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
