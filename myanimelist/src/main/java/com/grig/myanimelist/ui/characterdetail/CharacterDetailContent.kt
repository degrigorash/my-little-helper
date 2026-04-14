package com.grig.myanimelist.ui.characterdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grig.core.theme.AppTheme
import com.grig.myanimelist.data.model.jikan.JikanCharacterFull
import com.grig.myanimelist.ui.home.StatusBadge

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CharacterDetailContent(
    character: JikanCharacterFull,
    titleAlpha: Float = 1f
) {
    Column {
        Text(
            text = character.name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.graphicsLayer { alpha = titleAlpha }
        )

        if (character.nameKanji != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = character.nameKanji,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (character.nicknames.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                character.nicknames.forEach { nickname ->
                    StatusBadge(
                        text = nickname,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }

        if (character.favorites > 0) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "${character.favorites} favorites",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (!character.about.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = character.about,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        if (character.anime.isNotEmpty()) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Anime Appearances",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            character.anime.forEach { entry ->
                Text(
                    text = "${entry.anime.title} (${entry.role})",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }

        if (character.manga.isNotEmpty()) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Manga Appearances",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            character.manga.forEach { entry ->
                Text(
                    text = "${entry.manga.title} (${entry.role})",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }

        if (character.voices.isNotEmpty()) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Voice Actors",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            character.voices.forEach { voice ->
                Text(
                    text = "${voice.person.name} (${voice.language})",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(name = "Character Detail Content")
@Composable
private fun CharacterDetailContentPreview() {
    AppTheme(darkTheme = false) {
        CharacterDetailContent(character = previewCharacterFull)
    }
}

@Preview(name = "Character Detail Content - Minimal")
@Composable
private fun CharacterDetailContentMinimalPreview() {
    AppTheme(darkTheme = false) {
        CharacterDetailContent(character = previewCharacterFullMinimal)
    }
}

@Preview(name = "Character Detail Content - Dark")
@Composable
private fun CharacterDetailContentDarkPreview() {
    AppTheme(darkTheme = true) {
        CharacterDetailContent(character = previewCharacterFull)
    }
}
