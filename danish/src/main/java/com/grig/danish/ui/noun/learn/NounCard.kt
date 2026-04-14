package com.grig.danish.ui.noun.learn

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grig.core.theme.DanishTheme
import com.grig.danish.data.model.Noun
import com.grig.danish.ui.DanishFormRow
import com.grig.danish.ui.LearnMode
import com.grig.danish.ui.SpeakButton
import com.grig.danish.ui.noun.sampleNoun
import com.grig.danish.ui.noun.sampleNounWithAlt

@Composable
fun NounCard(
    noun: Noun,
    mode: LearnMode,
    revealed: Boolean,
    onReveal: () -> Unit,
    onSpeak: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (mode) {
                LearnMode.EN_TO_DK -> {
                    QuestionEnglish(noun)
                    SpoilerDivider(revealed = revealed, onReveal = onReveal)
                    AnswerDanishForms(
                        noun = noun,
                        revealed = revealed,
                        onReveal = onReveal,
                        onSpeak = onSpeak
                    )
                }
                LearnMode.DK_TO_EN -> {
                    QuestionDanish(noun = noun, onSpeak = onSpeak)
                    SpoilerDivider(revealed = revealed, onReveal = onReveal)
                    AnswerEnglishAndForms(
                        noun = noun,
                        revealed = revealed,
                        onReveal = onReveal,
                        onSpeak = onSpeak
                    )
                }
            }
        }
    }
}

@Composable
private fun QuestionEnglish(noun: Noun) {
    Text(
        text = noun.english,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold
    )
    if (noun.alt.isNotEmpty()) {
        Text(
            text = "Also: ${noun.alt.joinToString(", ")}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun QuestionDanish(noun: Noun, onSpeak: (String) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = noun.danish,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        SpeakButton(onClick = { onSpeak(noun.danish) })
    }
}

@Composable
private fun SpoilerDivider(revealed: Boolean, onReveal: () -> Unit) {
    if (revealed) {
        HorizontalDivider()
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onReveal),
            contentAlignment = Alignment.Center
        ) {
            HorizontalDivider()
            Text(
                text = "Tap to reveal",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun AnswerDanishForms(
    noun: Noun,
    revealed: Boolean,
    onReveal: () -> Unit,
    onSpeak: (String) -> Unit
) {
    SpoilerBox(revealed = revealed, onReveal = onReveal) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            DanishFormRow(
                label = "Danish",
                value = noun.danish,
                showSpeaker = revealed,
                onSpeak = onSpeak
            )
            DanishFormRow(
                label = "The form",
                value = noun.theForm,
                showSpeaker = revealed,
                onSpeak = onSpeak
            )
            DanishFormRow(
                label = "Plural",
                value = noun.plural,
                showSpeaker = revealed,
                onSpeak = onSpeak
            )
            DanishFormRow(
                label = "The plural",
                value = noun.thePlural,
                showSpeaker = revealed,
                onSpeak = onSpeak
            )

            if (noun.notes != null) {
                HorizontalDivider()
                Text(
                    text = noun.notes,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun AnswerEnglishAndForms(
    noun: Noun,
    revealed: Boolean,
    onReveal: () -> Unit,
    onSpeak: (String) -> Unit
) {
    SpoilerBox(revealed = revealed, onReveal = onReveal) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(
                text = noun.english,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            if (noun.alt.isNotEmpty()) {
                Text(
                    text = "Also: ${noun.alt.joinToString(", ")}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            DanishFormRow(
                label = "The form",
                value = noun.theForm,
                showSpeaker = revealed,
                onSpeak = onSpeak
            )
            DanishFormRow(
                label = "Plural",
                value = noun.plural,
                showSpeaker = revealed,
                onSpeak = onSpeak
            )
            DanishFormRow(
                label = "The plural",
                value = noun.thePlural,
                showSpeaker = revealed,
                onSpeak = onSpeak
            )

            if (noun.notes != null) {
                HorizontalDivider()
                Text(
                    text = noun.notes,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SpoilerBox(
    revealed: Boolean,
    onReveal: () -> Unit,
    content: @Composable () -> Unit
) {
    if (revealed) {
        AnimatedVisibility(
            visible = true,
            enter = fadeIn() + expandVertically()
        ) {
            content()
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onReveal)
                .blur(20.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
        ) {
            content()
        }
    }
}


@Preview(name = "EN→DK Hidden")
@Composable
private fun NounCardEnToDkHiddenPreview() {
    DanishTheme(darkTheme = false) {
        NounCard(noun = sampleNoun, mode = LearnMode.EN_TO_DK, revealed = false, onReveal = {}, onSpeak = {})
    }
}

@Preview(name = "EN→DK Revealed")
@Composable
private fun NounCardEnToDkRevealedPreview() {
    DanishTheme(darkTheme = false) {
        NounCard(noun = sampleNounWithAlt, mode = LearnMode.EN_TO_DK, revealed = true, onReveal = {}, onSpeak = {})
    }
}

@Preview(name = "DK→EN Hidden")
@Composable
private fun NounCardDkToEnHiddenPreview() {
    DanishTheme(darkTheme = false) {
        NounCard(noun = sampleNoun, mode = LearnMode.DK_TO_EN, revealed = false, onReveal = {}, onSpeak = {})
    }
}

@Preview(name = "DK→EN Revealed")
@Composable
private fun NounCardDkToEnRevealedPreview() {
    DanishTheme(darkTheme = false) {
        NounCard(noun = sampleNounWithAlt, mode = LearnMode.DK_TO_EN, revealed = true, onReveal = {}, onSpeak = {})
    }
}

@Preview(name = "EN→DK Hidden Dark")
@Composable
private fun NounCardEnToDkHiddenDarkPreview() {
    DanishTheme(darkTheme = true) {
        NounCard(noun = sampleNoun, mode = LearnMode.EN_TO_DK, revealed = false, onReveal = {}, onSpeak = {})
    }
}

@Preview(name = "DK→EN Revealed Dark")
@Composable
private fun NounCardDkToEnRevealedDarkPreview() {
    DanishTheme(darkTheme = true) {
        NounCard(noun = sampleNounWithAlt, mode = LearnMode.DK_TO_EN, revealed = true, onReveal = {}, onSpeak = {})
    }
}
