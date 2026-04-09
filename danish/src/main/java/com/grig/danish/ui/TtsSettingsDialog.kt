package com.grig.danish.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grig.core.theme.DanishTheme
import com.grig.danish.data.TtsSettings
import com.grig.danish.tools.TtsVoiceInfo

@Composable
fun TtsSettingsDialog(
    settings: TtsSettings,
    availableVoices: List<TtsVoiceInfo>,
    onSettingsChanged: (TtsSettings) -> Unit,
    onDismiss: () -> Unit
) {
    var rate by remember(settings) { mutableFloatStateOf(settings.speechRate) }
    var pitch by remember(settings) { mutableFloatStateOf(settings.pitch) }
    var selectedVoice by remember(settings) { mutableStateOf(settings.voiceName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Voice Settings",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                if (availableVoices.isNotEmpty()) {
                    VoicePicker(
                        voices = availableVoices,
                        selectedVoiceName = selectedVoice,
                        onVoiceSelected = { selectedVoice = it }
                    )
                }
                LabeledSlider(
                    label = "Speed",
                    value = rate,
                    valueRange = TtsSettings.MIN_RATE..TtsSettings.MAX_RATE,
                    valueLabel = "%.2f×".format(rate),
                    onValueChange = { rate = it }
                )
                LabeledSlider(
                    label = "Pitch",
                    value = pitch,
                    valueRange = TtsSettings.MIN_PITCH..TtsSettings.MAX_PITCH,
                    valueLabel = "%.2f×".format(pitch),
                    onValueChange = { pitch = it }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onSettingsChanged(
                    TtsSettings(speechRate = rate, pitch = pitch, voiceName = selectedVoice)
                )
                onDismiss()
            }) {
                Text("Apply")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VoicePicker(
    voices: List<TtsVoiceInfo>,
    selectedVoiceName: String?,
    onVoiceSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedLabel = voices.find { it.name == selectedVoiceName }?.label ?: "Default"

    Column {
        Text(
            text = "Voice",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = selectedLabel,
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                voices.forEach { voice ->
                    DropdownMenuItem(
                        text = { Text(voice.label, style = MaterialTheme.typography.bodyMedium) },
                        onClick = {
                            onVoiceSelected(voice.name)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun LabeledSlider(
    label: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    valueLabel: String,
    onValueChange: (Float) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = valueLabel,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        )
    }
}

@Preview
@Composable
private fun TtsSettingsDialogPreview() {
    DanishTheme(darkTheme = false) {
        TtsSettingsDialog(
            settings = TtsSettings(),
            availableVoices = listOf(
                TtsVoiceInfo("da-dk-voice1", "da-dk-voice1 (High)"),
                TtsVoiceInfo("da-dk-voice2", "da-dk-voice2 (Normal, Online)")
            ),
            onSettingsChanged = {},
            onDismiss = {}
        )
    }
}

@Preview
@Composable
private fun TtsSettingsDialogDarkPreview() {
    DanishTheme(darkTheme = true) {
        TtsSettingsDialog(
            settings = TtsSettings(speechRate = 0.75f, pitch = 1.2f),
            availableVoices = emptyList(),
            onSettingsChanged = {},
            onDismiss = {}
        )
    }
}
