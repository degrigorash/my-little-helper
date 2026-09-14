package com.grig.myanimelist.ui.seasons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grig.core.theme.AppTheme
import com.grig.core.theme.AppThemeExtended

@Composable
fun SeasonHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = AppThemeExtended.colorScheme.headerText,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
    )
}

@Preview(name = "Season Header")
@Composable
private fun SeasonHeaderPreview() {
    AppTheme(darkTheme = false) {
        SeasonHeader(title = "Summer 2026")
    }
}

@Preview(name = "Season Header - Dark")
@Composable
private fun SeasonHeaderDarkPreview() {
    AppTheme(darkTheme = true) {
        SeasonHeader(title = "Date TBA")
    }
}
