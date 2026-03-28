package com.grig.myanimelist.ui.characters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grig.core.theme.AppTheme
import com.grig.myanimelist.data.model.jikan.JikanCharacterEntry

@Composable
fun CharactersList(
    characters: List<JikanCharacterEntry>,
    onCharacterClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(characters, key = { it.character.malId }) { entry ->
            CharacterCard(
                entry = entry,
                onClick = { onCharacterClick(entry.character.malId) }
            )
        }
    }
}

@Preview(name = "Characters List")
@Composable
private fun CharactersListPreview() {
    AppTheme(darkTheme = false) {
        CharactersList(
            characters = previewCharacterEntries,
            onCharacterClick = {}
        )
    }
}

@Preview(name = "Characters List - Dark")
@Composable
private fun CharactersListDarkPreview() {
    AppTheme(darkTheme = true) {
        CharactersList(
            characters = previewCharacterEntries,
            onCharacterClick = {}
        )
    }
}
