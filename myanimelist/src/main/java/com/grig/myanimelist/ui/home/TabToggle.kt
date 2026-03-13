package com.grig.myanimelist.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grig.core.theme.AppTheme

@Composable
fun TabToggle(
    activeTab: MalTab,
    onTabSelected: (MalTab) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            MalTab.entries.forEach { tab ->
                val selected = tab == activeTab
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onTabSelected(tab) },
                    shape = RoundedCornerShape(50),
                    color = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent
                ) {
                    Text(
                        text = tab.name,
                        modifier = Modifier
                            .padding(vertical = 12.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        color = if (selected) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            }
        }
    }
}

@Preview(name = "Anime Selected")
@Composable
private fun TabToggleAnimePreview() {
    AppTheme(darkTheme = false) {
        TabToggle(activeTab = MalTab.Anime, onTabSelected = {})
    }
}

@Preview(name = "Manga Selected")
@Composable
private fun TabToggleMangaPreview() {
    AppTheme(darkTheme = false) {
        TabToggle(activeTab = MalTab.Manga, onTabSelected = {})
    }
}
