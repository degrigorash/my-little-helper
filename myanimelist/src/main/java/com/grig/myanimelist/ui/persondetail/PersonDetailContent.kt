package com.grig.myanimelist.ui.persondetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.grig.core.theme.AppTheme
import com.grig.myanimelist.data.model.jikan.PersonDetail

@Composable
fun PersonDetailContent(
    person: PersonDetail,
    onCharacterClick: (Int) -> Unit,
    listState: LazyListState = rememberLazyListState(),
    imageSpacerHeight: Dp = 0.dp,
    titleAlpha: Float = 1f
) {
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (imageSpacerHeight > 0.dp) {
            item(key = "image_spacer") {
                Spacer(modifier = Modifier.height(imageSpacerHeight))
            }
        }

        item(key = "person_header") {
            PersonInfoHeader(person = person, titleAlpha = titleAlpha)
        }

        if (person.characters.isNotEmpty()) {
            item(key = "characters_title") {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Characters (${person.characters.size})",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            items(
                person.characters,
                key = { it.character.malId }
            ) { voicedCharacter ->
                PersonCharacterItem(
                    voicedCharacter = voicedCharacter,
                    onClick = { onCharacterClick(voicedCharacter.character.malId) }
                )
            }
        }
    }
}

@Composable
private fun PersonInfoHeader(
    person: PersonDetail,
    titleAlpha: Float = 1f
) {
    val info = person.person
    Column {
        Text(
            text = info.name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.graphicsLayer { alpha = titleAlpha }
        )

        val japaneseName = listOfNotNull(info.familyName, info.givenName)
            .joinToString(" ")
            .takeIf { it.isNotBlank() }
        if (japaneseName != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = japaneseName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        val birthday = info.birthday?.take(10)
        if (birthday != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Born: $birthday",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (info.favorites > 0) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${info.favorites} favorites",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (!info.about.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = info.about,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(name = "Person Detail Content")
@Composable
private fun PersonDetailContentPreview() {
    AppTheme(darkTheme = false) {
        PersonDetailContent(person = previewPersonDetail, onCharacterClick = {})
    }
}

@Preview(name = "Person Detail Content - Dark")
@Composable
private fun PersonDetailContentDarkPreview() {
    AppTheme(darkTheme = true) {
        PersonDetailContent(person = previewPersonDetail, onCharacterClick = {})
    }
}
