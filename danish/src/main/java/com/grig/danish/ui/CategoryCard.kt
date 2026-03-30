package com.grig.danish.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grig.core.theme.AppTheme

@Composable
fun CategoryCard(
    title: String,
    wordCount: Int,
    enabled: Boolean,
    onLearnClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(onClick = onLearnClick) {
                        Text("Learn")
                    }
                    OutlinedButton(onClick = {}, enabled = false) {
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
    AppTheme(darkTheme = false) {
        CategoryCard(
            title = "Nouns",
            wordCount = 42,
            enabled = true,
            onLearnClick = {}
        )
    }
}

@Preview(name = "Dark - Enabled")
@Composable
private fun CategoryCardEnabledDarkPreview() {
    AppTheme(darkTheme = true) {
        CategoryCard(
            title = "Nouns",
            wordCount = 42,
            enabled = true,
            onLearnClick = {}
        )
    }
}

@Preview(name = "Light - Disabled")
@Composable
private fun CategoryCardDisabledPreview() {
    AppTheme(darkTheme = false) {
        CategoryCard(
            title = "Verbs",
            wordCount = 0,
            enabled = false,
            onLearnClick = {}
        )
    }
}

@Preview(name = "Dark - Disabled")
@Composable
private fun CategoryCardDisabledDarkPreview() {
    AppTheme(darkTheme = true) {
        CategoryCard(
            title = "Verbs",
            wordCount = 0,
            enabled = false,
            onLearnClick = {}
        )
    }
}
