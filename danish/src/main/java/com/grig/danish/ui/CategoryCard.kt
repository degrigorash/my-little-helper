package com.grig.danish.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grig.core.theme.DanishTheme
import com.grig.danish.R

@Composable
fun CategoryCard(
    title: String,
    wordCount: Int,
    enabled: Boolean,
    onLearnClick: (LearnMode, Boolean) -> Unit,
    onPracticeClick: (LearnMode, Boolean) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    var selectedMode by rememberSaveable { mutableStateOf(LearnMode.DK_TO_EN) }
    var shuffled by rememberSaveable { mutableStateOf(true) }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                if (enabled) {
                    Text(
                        text = "$wordCount words",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (enabled) {
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    LearnMode.entries.forEachIndexed { index, mode ->
                        val isSelected = selectedMode == mode
                        SegmentedButton(
                            selected = isSelected,
                            onClick = { selectedMode = mode },
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = LearnMode.entries.size
                            ),
                            icon = {},
                            colors = SegmentedButtonDefaults.colors(
                                activeContainerColor = MaterialTheme.colorScheme.primary,
                                activeContentColor = MaterialTheme.colorScheme.onPrimary,
                                activeBorderColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(
                                when (mode) {
                                    LearnMode.DK_TO_EN -> "DK → EN"
                                    LearnMode.EN_TO_DK -> "EN → DK"
                                }
                            )
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilledIconToggleButton(
                        checked = shuffled,
                        onCheckedChange = { shuffled = it },
                        shape = CircleShape,
                        modifier = Modifier.size(40.dp),
                        colors = IconButtonDefaults.filledIconToggleButtonColors(
                            checkedContainerColor = MaterialTheme.colorScheme.primary,
                            checkedContentColor = MaterialTheme.colorScheme.onPrimary,
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_shuffle),
                            contentDescription = if (shuffled) "Random order" else "Sequential order",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Button(onClick = { onLearnClick(selectedMode, shuffled) }) {
                        Text("Learn")
                    }
                    Button(onClick = { onPracticeClick(selectedMode, shuffled) }) {
                        Text("Practice")
                    }
                }
            } else {
                Text(
                    text = "Coming soon",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(name = "Light - Enabled")
@Composable
private fun CategoryCardEnabledPreview() {
    DanishTheme(darkTheme = false) {
        CategoryCard(
            title = "Nouns",
            wordCount = 42,
            enabled = true,
            onLearnClick = { _, _ -> },
            onPracticeClick = { _, _ -> }
        )
    }
}

@Preview(name = "Dark - Enabled")
@Composable
private fun CategoryCardEnabledDarkPreview() {
    DanishTheme(darkTheme = true) {
        CategoryCard(
            title = "Nouns",
            wordCount = 42,
            enabled = true,
            onLearnClick = { _, _ -> },
            onPracticeClick = { _, _ -> }
        )
    }
}

@Preview(name = "Light - Disabled")
@Composable
private fun CategoryCardDisabledPreview() {
    DanishTheme(darkTheme = false) {
        CategoryCard(
            title = "Verbs",
            wordCount = 0,
            enabled = false,
            onLearnClick = { _, _ -> }
        )
    }
}

@Preview(name = "Dark - Disabled")
@Composable
private fun CategoryCardDisabledDarkPreview() {
    DanishTheme(darkTheme = true) {
        CategoryCard(
            title = "Verbs",
            wordCount = 0,
            enabled = false,
            onLearnClick = { _, _ -> }
        )
    }
}
