package com.grig.myanimelist.ui.authordetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.grig.core.theme.AppTheme
import com.grig.myanimelist.data.model.jikan.JikanPersonFull

@Composable
fun AuthorDetailContent(
    person: JikanPersonFull,
    onAnimeClick: (Int) -> Unit,
    onMangaClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        val imageUrl = person.images?.jpg?.imageUrl
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = person.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Text(
            text = person.name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        val altNames = buildList {
            person.givenName?.let { add(it) }
            person.familyName?.let { add(it) }
        }
        if (altNames.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = altNames.joinToString(" "),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        val birthday = person.birthday?.take(10)
        if (birthday != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Born: $birthday",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (person.favorites > 0) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${person.favorites} favorites",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (!person.about.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = person.about,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        if (person.manga.isNotEmpty()) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Manga",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            person.manga.forEach { entry ->
                AuthorMediaItem(
                    title = entry.manga.title,
                    role = entry.position,
                    imageUrl = entry.manga.images?.jpg?.imageUrl
                        ?: entry.manga.images?.webp?.imageUrl,
                    score = entry.manga.score,
                    onClick = { onMangaClick(entry.manga.malId) }
                )
            }
        }

        if (person.anime.isNotEmpty()) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Anime",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            person.anime.forEach { entry ->
                AuthorMediaItem(
                    title = entry.anime.title,
                    role = entry.position,
                    imageUrl = entry.anime.images?.jpg?.imageUrl
                        ?: entry.anime.images?.webp?.imageUrl,
                    score = entry.anime.score,
                    onClick = { onAnimeClick(entry.anime.malId) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(name = "Author Detail Content")
@Composable
private fun AuthorDetailContentPreview() {
    AppTheme(darkTheme = false) {
        AuthorDetailContent(
            person = previewPersonFull,
            onAnimeClick = {},
            onMangaClick = {}
        )
    }
}

@Preview(name = "Author Detail Content - Dark")
@Composable
private fun AuthorDetailContentDarkPreview() {
    AppTheme(darkTheme = true) {
        AuthorDetailContent(
            person = previewPersonFull,
            onAnimeClick = {},
            onMangaClick = {}
        )
    }
}
