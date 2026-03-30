package com.grig.danish.ui.noun

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grig.core.theme.AppTheme
import com.grig.danish.data.model.Noun

@Composable
fun NounCard(
    noun: Noun,
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
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = noun.english,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            if (noun.alt.isNotEmpty()) {
                Text(
                    text = "Also: ${noun.alt.joinToString(", ")}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            HorizontalDivider()

            NounFormRow(label = "Danish", value = noun.danish)
            NounFormRow(label = "The form", value = noun.theForm)
            NounFormRow(label = "Plural", value = noun.plural)
            NounFormRow(label = "The plural", value = noun.thePlural)

            if (noun.notes != null) {
                HorizontalDivider()
                Text(
                    text = noun.notes,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun NounFormRow(
    label: String,
    value: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(name = "Light")
@Composable
private fun NounCardPreview() {
    AppTheme(darkTheme = false) {
        NounCard(noun = sampleNoun)
    }
}

@Preview(name = "Dark")
@Composable
private fun NounCardDarkPreview() {
    AppTheme(darkTheme = true) {
        NounCard(noun = sampleNoun)
    }
}

@Preview(name = "Light - With Alt & Notes")
@Composable
private fun NounCardWithAltPreview() {
    AppTheme(darkTheme = false) {
        NounCard(noun = sampleNounWithAlt)
    }
}

@Preview(name = "Dark - With Alt & Notes")
@Composable
private fun NounCardWithAltDarkPreview() {
    AppTheme(darkTheme = true) {
        NounCard(noun = sampleNounWithAlt)
    }
}
