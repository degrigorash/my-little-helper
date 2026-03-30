package com.grig.danish.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grig.core.theme.DanishTheme
import com.grig.danish.R

@Composable
fun DanishFormRow(
    label: String,
    value: String,
    onSpeak: (String) -> Unit,
    showSpeaker: Boolean = true
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
        }
        if (showSpeaker) SpeakButton(onClick = { onSpeak(value) })
    }
}

@Composable
fun SpeakButton(onClick: () -> Unit) {
    IconButton(onClick = onClick, modifier = Modifier.size(32.dp)) {
        Icon(
            painter = painterResource(R.drawable.ic_talk),
            contentDescription = "Listen",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Preview(name = "Form Row - Light")
@Composable
private fun DanishFormRowPreview() {
    DanishTheme(darkTheme = false) {
        DanishFormRow(label = "The form", value = "bogen", onSpeak = {})
    }
}

@Preview(name = "Form Row - Dark")
@Composable
private fun DanishFormRowDarkPreview() {
    DanishTheme(darkTheme = true) {
        DanishFormRow(label = "The form", value = "bogen", onSpeak = {})
    }
}

@Preview(name = "Form Row - No Speaker")
@Composable
private fun DanishFormRowNoSpeakerPreview() {
    DanishTheme(darkTheme = false) {
        DanishFormRow(label = "Plural", value = "bøger", onSpeak = {}, showSpeaker = false)
    }
}
