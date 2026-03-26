package com.grig.myanimelist.ui.mangaedit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import com.grig.myanimelist.data.model.manga.MalMangaReadingStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MangaStatusDropdown(
    selected: MalMangaReadingStatus,
    onSelected: (MalMangaReadingStatus) -> Unit,
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
            MalMangaReadingStatus.entries.forEach { status ->
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
fun ChapterInput(
    read: Int,
    total: Int?,
    onReadChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        OutlinedTextField(
            value = if (read == 0) "" else read.toString(),
            onValueChange = { text ->
                val num = text.filter { it.isDigit() }.toIntOrNull() ?: 0
                val clamped = if (total != null && total > 0) num.coerceIn(0, total) else num.coerceAtLeast(0)
                onReadChange(clamped)
            },
            label = { Text("Chapters") },
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

@Composable
fun VolumeInput(
    read: Int,
    total: Int?,
    onReadChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        OutlinedTextField(
            value = if (read == 0) "" else read.toString(),
            onValueChange = { text ->
                val num = text.filter { it.isDigit() }.toIntOrNull() ?: 0
                val clamped = if (total != null && total > 0) num.coerceIn(0, total) else num.coerceAtLeast(0)
                onReadChange(clamped)
            },
            label = { Text("Volumes") },
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

@Preview(name = "Manga Status Dropdown")
@Composable
private fun MangaStatusDropdownPreview() {
    AppTheme(darkTheme = false) {
        MangaStatusDropdown(
            selected = MalMangaReadingStatus.Reading,
            onSelected = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(name = "Chapter Input")
@Composable
private fun ChapterInputPreview() {
    AppTheme(darkTheme = false) {
        ChapterInput(read = 45, total = 100, onReadChange = {})
    }
}

@Preview(name = "Volume Input")
@Composable
private fun VolumeInputPreview() {
    AppTheme(darkTheme = false) {
        VolumeInput(read = 5, total = 12, onReadChange = {})
    }
}

@Preview(name = "Manga Status Dropdown - Dark")
@Composable
private fun MangaStatusDropdownDarkPreview() {
    AppTheme(darkTheme = true) {
        MangaStatusDropdown(
            selected = MalMangaReadingStatus.Completed,
            onSelected = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}
