package com.grig.myanimelist.ui.animeedit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.grig.myanimelist.R
import com.grig.myanimelist.ui.animelist.AnimeCardData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAnimeBottomSheet(
    data: AnimeCardData,
    viewModel: EditAnimeViewModel,
    onDismiss: () -> Unit,
    onSaved: (EditAnimeEvent.Saved) -> Unit,
    onDeleted: () -> Unit,
    onOpenDetail: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSaveConfirm by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    LaunchedEffect(data) {
        viewModel.init(data)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is EditAnimeEvent.Saved -> onSaved(event)
                is EditAnimeEvent.Deleted -> onDeleted()
                is EditAnimeEvent.Error -> {}
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .navigationBarsPadding()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = data.anime.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = viewModel::toggleBookmarkEditor) {
                    Icon(
                        painter = painterResource(
                            if (state.isBookmarked) R.drawable.ic_bookmark
                            else R.drawable.ic_bookmark_border
                        ),
                        contentDescription = "Watch Next",
                        tint = if (state.isBookmarked) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            AnimatedVisibility(visible = state.showBookmarkEditor) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = state.bookmarkNotes,
                        onValueChange = viewModel::setBookmarkNotes,
                        label = { Text("Notes (optional)") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        maxLines = 4
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (state.isBookmarked) {
                            OutlinedButton(
                                onClick = { viewModel.removeBookmark(data.anime.id) },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.error
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Remove")
                            }
                        }
                        Button(
                            onClick = { viewModel.saveBookmark(data.anime.id) },
                            enabled = !state.isSaving,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(if (state.isBookmarked) "Update" else "Add to Watch Next")
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            StatusDropdown(
                selected = state.status,
                onSelected = viewModel::setStatus,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            EpisodeInput(
                watched = state.numEpisodesWatched,
                total = data.anime.numEpisodes,
                onWatchedChange = viewModel::setEpisodes
            )

            Spacer(modifier = Modifier.height(12.dp))

            ScoreDropdown(
                score = state.score,
                onScoreSelected = viewModel::setScore,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            FinishDateRow(
                finishDate = state.finishDate,
                onSetToday = { viewModel.setFinishDateToday(data.anime.numEpisodes) }
            )

            if (state.error != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = state.error!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { showSaveConfirm = true },
                    enabled = !state.isSaving,
                    modifier = Modifier.weight(1f)
                ) {
                    if (state.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Save")
                    }
                }
                Button(
                    onClick = { showDeleteConfirm = true },
                    enabled = !state.isSaving,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Delete")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onOpenDetail,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View Details")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (showSaveConfirm) {
        ConfirmationDialog(
            title = "Save Changes",
            message = "Save your changes to \"${data.anime.title}\"?",
            confirmText = "Save",
            onConfirm = {
                showSaveConfirm = false
                viewModel.save(data.anime.id)
            },
            onDismiss = { showSaveConfirm = false }
        )
    }

    if (showDeleteConfirm) {
        ConfirmationDialog(
            title = "Delete Entry",
            message = "Remove \"${data.anime.title}\" from your list? This cannot be undone.",
            confirmText = "Delete",
            onConfirm = {
                showDeleteConfirm = false
                viewModel.delete(data.anime.id)
            },
            onDismiss = { showDeleteConfirm = false },
            isDestructive = true
        )
    }
}
