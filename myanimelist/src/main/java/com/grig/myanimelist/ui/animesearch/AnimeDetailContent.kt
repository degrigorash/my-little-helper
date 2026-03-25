package com.grig.myanimelist.ui.animesearch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.grig.core.theme.AppTheme
import com.grig.myanimelist.data.model.anime.MalAnime
import com.grig.myanimelist.ui.animeedit.ConfirmationDialog
import com.grig.myanimelist.ui.home.StatusBadge
import com.grig.myanimelist.ui.home.StatsRow
import com.grig.myanimelist.ui.home.animeStatusColor
import com.grig.myanimelist.ui.home.buildAiredText
import com.grig.myanimelist.ui.animelist.previewAnime

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AnimeDetailContent(
    anime: MalAnime,
    authorized: Boolean,
    isInMyList: Boolean,
    isUpdatingList: Boolean,
    onAddToList: () -> Unit,
    onDeleteFromList: () -> Unit,
    onRelatedAnimeClick: (Int) -> Unit = {}
) {
    var showDeleteConfirm by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        if (anime.pictures != null) {
            AsyncImage(
                model = anime.pictures.large ?: anime.pictures.medium,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Text(
            text = anime.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        val altTitles = buildList {
            anime.alternativeTitles.en?.takeIf { it.isNotBlank() && it != anime.title }
                ?.let { add(it) }
            anime.alternativeTitles.ja?.takeIf { it.isNotBlank() }?.let { add(it) }
        }
        if (altTitles.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = altTitles.joinToString(" / "),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatusBadge(
                text = anime.status.displayName,
                color = animeStatusColor(anime.status)
            )
            val airedText = buildAiredText(anime.startDate, anime.endDate)
            if (airedText != null) {
                Text(
                    text = airedText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        StatsRow(mean = anime.mean, rank = anime.rank, members = anime.numListUsers)

        val epCount = anime.numEpisodes?.takeIf { it > 0 }
        if (epCount != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$epCount episodes",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (anime.synopsis.isNotBlank()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = anime.synopsis,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        if (anime.genres.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                anime.genres.forEach { genre ->
                    StatusBadge(
                        text = genre.name,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        val studioNames = anime.studios.joinToString(", ") { it.name }
        if (studioNames.isNotBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = studioNames,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (authorized) {
            Spacer(modifier = Modifier.height(24.dp))
            if (isInMyList) {
                Button(
                    onClick = { showDeleteConfirm = true },
                    enabled = !isUpdatingList,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    if (isUpdatingList) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onError
                        )
                    } else {
                        Text("Delete from My List")
                    }
                }
            } else {
                Button(
                    onClick = onAddToList,
                    enabled = !isUpdatingList,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    if (isUpdatingList) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Add to My List")
                    }
                }
            }
        }

        if (anime.relatedAnime.isNotEmpty()) {
            RelatedAnimeSection(
                relatedAnime = anime.relatedAnime,
                onRelatedAnimeClick = onRelatedAnimeClick
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }

    if (showDeleteConfirm) {
        ConfirmationDialog(
            title = "Remove from List",
            message = "Remove \"${anime.title}\" from your list?",
            confirmText = "Delete",
            onConfirm = {
                showDeleteConfirm = false
                onDeleteFromList()
            },
            onDismiss = { showDeleteConfirm = false },
            isDestructive = true
        )
    }
}

@Preview(name = "Anime Detail - Not in List")
@Composable
private fun AnimeDetailContentPreview() {
    AppTheme(darkTheme = false) {
        AnimeDetailContent(
            anime = previewAnime,
            authorized = true,
            isInMyList = false,
            isUpdatingList = false,
            onAddToList = {},
            onDeleteFromList = {}
        )
    }
}

@Preview(name = "Anime Detail - In List")
@Composable
private fun AnimeDetailContentInListPreview() {
    AppTheme(darkTheme = false) {
        AnimeDetailContent(
            anime = previewAnime,
            authorized = true,
            isInMyList = true,
            isUpdatingList = false,
            onAddToList = {},
            onDeleteFromList = {}
        )
    }
}

@Preview(name = "Anime Detail - Guest")
@Composable
private fun AnimeDetailContentGuestPreview() {
    AppTheme(darkTheme = false) {
        AnimeDetailContent(
            anime = previewAnime,
            authorized = false,
            isInMyList = false,
            isUpdatingList = false,
            onAddToList = {},
            onDeleteFromList = {}
        )
    }
}

@Preview(name = "Anime Detail - Dark")
@Composable
private fun AnimeDetailContentDarkPreview() {
    AppTheme(darkTheme = true) {
        AnimeDetailContent(
            anime = previewAnime,
            authorized = true,
            isInMyList = false,
            isUpdatingList = false,
            onAddToList = {},
            onDeleteFromList = {}
        )
    }
}
