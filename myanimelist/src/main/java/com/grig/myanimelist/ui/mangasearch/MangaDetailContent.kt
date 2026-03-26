package com.grig.myanimelist.ui.mangasearch

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
import androidx.compose.material3.OutlinedButton
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
import com.grig.myanimelist.data.model.jikan.ResolvedRelation
import com.grig.myanimelist.data.model.manga.MalManga
import com.grig.myanimelist.ui.animeedit.ConfirmationDialog
import com.grig.myanimelist.ui.common.CrossMediaRelationsSection
import com.grig.myanimelist.ui.home.StatusBadge
import com.grig.myanimelist.ui.home.StatsRow
import com.grig.myanimelist.ui.home.buildAiredText
import com.grig.myanimelist.ui.home.mangaStatusColor
import com.grig.myanimelist.ui.mangalist.previewManga

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MangaDetailContent(
    manga: MalManga,
    authorized: Boolean,
    isInMyList: Boolean,
    isUpdatingList: Boolean,
    onAddToList: () -> Unit,
    onDeleteFromList: () -> Unit,
    onRelatedMangaClick: (Int) -> Unit = {},
    relatedAnime: List<ResolvedRelation> = emptyList(),
    isLoadingRelatedAnime: Boolean = false,
    onRelatedAnimeClick: (Int) -> Unit = {},
    onReviewsClick: () -> Unit = {}
) {
    var showDeleteConfirm by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        if (manga.pictures != null) {
            AsyncImage(
                model = manga.pictures.large ?: manga.pictures.medium,
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
            text = manga.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        val altTitles = buildList {
            manga.alternativeTitles.en?.takeIf { it.isNotBlank() && it != manga.title }
                ?.let { add(it) }
            manga.alternativeTitles.ja?.takeIf { it.isNotBlank() }?.let { add(it) }
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
                text = manga.status.displayName,
                color = mangaStatusColor(manga.status)
            )
            val dateText = buildAiredText(manga.startDate, manga.endDate)
            if (dateText != null) {
                Text(
                    text = dateText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        StatsRow(mean = manga.mean, rank = manga.rank, members = manga.numListUsers)

        val chapterCount = manga.numChapters?.takeIf { it > 0 }
        val volumeCount = manga.numVolumes?.takeIf { it > 0 }
        val countParts = buildList {
            if (chapterCount != null) add("$chapterCount chapters")
            if (volumeCount != null) add("$volumeCount volumes")
        }
        if (countParts.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = countParts.joinToString(" \u00B7 "),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        val authorNames = manga.authors
            .map { it.author.displayName }
            .distinct()
            .joinToString(", ")
        if (authorNames.isNotBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = authorNames,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (manga.synopsis.isNotBlank()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = manga.synopsis,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        if (manga.genres.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                manga.genres.forEach { genre ->
                    StatusBadge(
                        text = genre.name,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
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

        if (manga.relatedManga.isNotEmpty()) {
            RelatedMangaSection(
                relatedManga = manga.relatedManga,
                onRelatedMangaClick = onRelatedMangaClick
            )
        }

        CrossMediaRelationsSection(
            title = "Related Anime",
            relations = relatedAnime,
            isLoading = isLoadingRelatedAnime,
            onClick = onRelatedAnimeClick
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedButton(
            onClick = onReviewsClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Reviews")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }

    if (showDeleteConfirm) {
        ConfirmationDialog(
            title = "Remove from List",
            message = "Remove \"${manga.title}\" from your list?",
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

@Preview(name = "Manga Detail - Not in List")
@Composable
private fun MangaDetailContentPreview() {
    AppTheme(darkTheme = false) {
        MangaDetailContent(
            manga = previewManga,
            authorized = true,
            isInMyList = false,
            isUpdatingList = false,
            onAddToList = {},
            onDeleteFromList = {}
        )
    }
}

@Preview(name = "Manga Detail - In List")
@Composable
private fun MangaDetailContentInListPreview() {
    AppTheme(darkTheme = false) {
        MangaDetailContent(
            manga = previewManga,
            authorized = true,
            isInMyList = true,
            isUpdatingList = false,
            onAddToList = {},
            onDeleteFromList = {}
        )
    }
}

@Preview(name = "Manga Detail - Guest")
@Composable
private fun MangaDetailContentGuestPreview() {
    AppTheme(darkTheme = false) {
        MangaDetailContent(
            manga = previewManga,
            authorized = false,
            isInMyList = false,
            isUpdatingList = false,
            onAddToList = {},
            onDeleteFromList = {}
        )
    }
}

@Preview(name = "Manga Detail - Dark")
@Composable
private fun MangaDetailContentDarkPreview() {
    AppTheme(darkTheme = true) {
        MangaDetailContent(
            manga = previewManga,
            authorized = true,
            isInMyList = false,
            isUpdatingList = false,
            onAddToList = {},
            onDeleteFromList = {}
        )
    }
}
