package com.grig.myanimelist.ui.animeedit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grig.core.theme.AppTheme
import com.grig.myanimelist.data.model.anime.MalAnimeWatchingStatus
import com.grig.myanimelist.ui.home.formatApiDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusDropdown(
    selected: MalAnimeWatchingStatus,
    onSelected: (MalAnimeWatchingStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selected.displayName,
            onValueChange = {},
            readOnly = true,
            label = { Text("Status") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            MalAnimeWatchingStatus.entries.forEach { status ->
                DropdownMenuItem(
                    text = { Text(status.displayName) },
                    onClick = {
                        onSelected(status)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun EpisodeInput(
    watched: Int,
    total: Int?,
    onWatchedChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        OutlinedTextField(
            value = if (watched == 0) "" else watched.toString(),
            onValueChange = { text ->
                val num = text.filter { it.isDigit() }.toIntOrNull() ?: 0
                val clamped = if (total != null && total > 0) num.coerceIn(0, total) else num.coerceAtLeast(0)
                onWatchedChange(clamped)
            },
            label = { Text("Episodes") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.width(120.dp)
        )
        Text(
            text = "/ ${total?.takeIf { it > 0 } ?: "?"}",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoreDropdown(
    score: Int,
    onScoreSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val labels = listOf(
        0 to "Select score",
        1 to "1 - Appalling",
        2 to "2 - Horrible",
        3 to "3 - Very Bad",
        4 to "4 - Bad",
        5 to "5 - Average",
        6 to "6 - Fine",
        7 to "7 - Good",
        8 to "8 - Very Good",
        9 to "9 - Great",
        10 to "10 - Masterpiece"
    )

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = labels.first { it.first == score }.second,
            onValueChange = {},
            readOnly = true,
            label = { Text("Your Score") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            labels.forEach { (value, label) ->
                DropdownMenuItem(
                    text = { Text(label) },
                    onClick = {
                        onScoreSelected(value)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun FinishDateRow(
    finishDate: String?,
    onSetToday: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
    ) {
        Text(
            text = "Finish Date:",
            style = MaterialTheme.typography.bodyLarge
        )
        if (finishDate != null) {
            Text(
                text = formatApiDate(finishDate) ?: finishDate,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        } else {
            Button(onClick = onSetToday) {
                Text("Today")
            }
        }
    }
}

@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    confirmText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isDestructive: Boolean = false
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = if (isDestructive) {
                    ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                } else {
                    ButtonDefaults.textButtonColors()
                }
            ) {
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Preview(name = "Status Dropdown")
@Composable
private fun StatusDropdownPreview() {
    AppTheme(darkTheme = false) {
        StatusDropdown(
            selected = MalAnimeWatchingStatus.Watching,
            onSelected = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(name = "Episode Input")
@Composable
private fun EpisodeInputPreview() {
    AppTheme(darkTheme = false) {
        EpisodeInput(watched = 12, total = 24, onWatchedChange = {})
    }
}

@Preview(name = "Score Dropdown")
@Composable
private fun ScoreDropdownPreview() {
    AppTheme(darkTheme = false) {
        ScoreDropdown(
            score = 8,
            onScoreSelected = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(name = "Finish Date - Set")
@Composable
private fun FinishDateSetPreview() {
    AppTheme(darkTheme = false) {
        FinishDateRow(finishDate = "2025-06-15", onSetToday = {})
    }
}

@Preview(name = "Finish Date - Not Set")
@Composable
private fun FinishDateNotSetPreview() {
    AppTheme(darkTheme = false) {
        FinishDateRow(finishDate = null, onSetToday = {})
    }
}

@Preview(name = "Confirmation Dialog")
@Composable
private fun ConfirmationDialogPreview() {
    AppTheme(darkTheme = false) {
        ConfirmationDialog(
            title = "Save Changes",
            message = "Save your changes?",
            confirmText = "Save",
            onConfirm = {},
            onDismiss = {}
        )
    }
}

@Preview(name = "Confirmation Dialog - Dark Destructive")
@Composable
private fun ConfirmationDialogDarkPreview() {
    AppTheme(darkTheme = true) {
        ConfirmationDialog(
            title = "Delete Entry",
            message = "Remove this entry?",
            confirmText = "Delete",
            onConfirm = {},
            onDismiss = {},
            isDestructive = true
        )
    }
}
