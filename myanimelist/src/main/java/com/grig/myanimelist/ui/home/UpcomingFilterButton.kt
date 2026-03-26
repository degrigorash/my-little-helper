package com.grig.myanimelist.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grig.core.theme.AppTheme
import com.grig.core.theme.AppThemeExtended
import com.grig.myanimelist.R

@Composable
fun UpcomingFilterButton(
    selected: Boolean,
    onClick: () -> Unit
) {
    val colors = AppThemeExtended.colorScheme

    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .then(
                if (selected) {
                    Modifier.background(colors.malCardStart)
                } else {
                    Modifier.border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                }
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_upcoming),
            contentDescription = "Upcoming",
            modifier = Modifier.size(18.dp),
            tint = if (selected) colors.cardText else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(name = "Upcoming Filter - Unselected")
@Composable
private fun UpcomingFilterButtonPreview() {
    AppTheme(darkTheme = false) {
        UpcomingFilterButton(selected = false, onClick = {})
    }
}

@Preview(name = "Upcoming Filter - Selected")
@Composable
private fun UpcomingFilterButtonSelectedPreview() {
    AppTheme(darkTheme = false) {
        UpcomingFilterButton(selected = true, onClick = {})
    }
}

@Preview(name = "Upcoming Filter - Dark")
@Composable
private fun UpcomingFilterButtonDarkPreview() {
    AppTheme(darkTheme = true) {
        UpcomingFilterButton(selected = true, onClick = {})
    }
}
