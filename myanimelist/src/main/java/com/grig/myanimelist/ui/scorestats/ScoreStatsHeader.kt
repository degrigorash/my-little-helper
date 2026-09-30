package com.grig.myanimelist.ui.scorestats

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grig.core.theme.AppTheme
import com.grig.myanimelist.R

@Composable
fun ScoreStatsHeader(
    mean: Float,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_star),
            contentDescription = null,
            modifier = Modifier.size(28.dp),
            tint = Color(0xFFFFC107)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = String.format("%.2f", mean),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onClose) {
            Icon(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = "Close",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(name = "Score Stats Header")
@Composable
private fun ScoreStatsHeaderPreview() {
    AppTheme(darkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.surfaceContainerHigh) {
            ScoreStatsHeader(
                mean = 8.43f,
                onClose = {},
                modifier = Modifier.padding(start = 24.dp, end = 12.dp)
            )
        }
    }
}

@Preview(name = "Score Stats Header - Dark")
@Composable
private fun ScoreStatsHeaderDarkPreview() {
    AppTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.surfaceContainerHigh) {
            ScoreStatsHeader(
                mean = 8.43f,
                onClose = {},
                modifier = Modifier.padding(start = 24.dp, end = 12.dp)
            )
        }
    }
}
