package com.grig.myanimelist.ui.characters

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.grig.core.theme.AppTheme
import com.grig.myanimelist.data.model.jikan.JikanCharacterEntry

@Composable
fun CharacterCard(
    entry: JikanCharacterEntry,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageUrl = entry.character.images?.jpg?.imageUrl
                ?: entry.character.images?.webp?.imageUrl
            if (imageUrl != null) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = entry.character.name,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.character.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = entry.role,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                val jaVa = entry.voiceActors.firstOrNull { it.language == "Japanese" }
                if (jaVa != null) {
                    Text(
                        text = "CV: ${jaVa.person.name}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Preview(name = "Character Card - Main")
@Composable
private fun CharacterCardPreview() {
    AppTheme(darkTheme = false) {
        CharacterCard(
            entry = previewCharacterEntry,
            onClick = {}
        )
    }
}

@Preview(name = "Character Card - Supporting")
@Composable
private fun CharacterCardSupportingPreview() {
    AppTheme(darkTheme = false) {
        CharacterCard(
            entry = previewCharacterEntrySupporting,
            onClick = {}
        )
    }
}

@Preview(name = "Character Card - Dark")
@Composable
private fun CharacterCardDarkPreview() {
    AppTheme(darkTheme = true) {
        CharacterCard(
            entry = previewCharacterEntry,
            onClick = {}
        )
    }
}
