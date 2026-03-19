package com.grig.myanimelist.ui.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grig.core.theme.AppTheme
import com.grig.core.theme.AppThemeExtended

@Composable
fun TabToggle(
    activeTab: MalTab,
    onTabSelected: (MalTab) -> Unit
) {
    val colors = AppThemeExtended.colorScheme
    val tabs = MalTab.entries
    val selectedIndex = tabs.indexOf(activeTab)

    val animatedOffset by animateFloatAsState(
        targetValue = selectedIndex.toFloat(),
        animationSpec = tween(durationMillis = 250),
        label = "tab_slide"
    )

    val indicatorColor = colors.malCardStart

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    val tabWidth = size.width / tabs.size
                    drawRoundRect(
                        color = indicatorColor,
                        topLeft = Offset(x = tabWidth * animatedOffset, y = 0f),
                        size = Size(width = tabWidth, height = size.height),
                        cornerRadius = CornerRadius(size.height / 2f)
                    )
                }
        ) {
            tabs.forEach { tab ->
                val selected = tab == activeTab
                val textColor by animateColorAsState(
                    targetValue = if (selected) colors.cardText
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                    animationSpec = tween(durationMillis = 250),
                    label = "tab_text_color"
                )

                Text(
                    text = tab.name,
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onTabSelected(tab) }
                        .padding(vertical = 12.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    color = textColor
                )
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
