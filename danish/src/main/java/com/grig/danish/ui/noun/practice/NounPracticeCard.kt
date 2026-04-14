package com.grig.danish.ui.noun.practice

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grig.core.theme.DanishTheme
import com.grig.danish.data.model.Noun
import com.grig.danish.ui.DanishFormRow
import com.grig.danish.ui.LearnMode
import com.grig.danish.ui.SpeakButton
import com.grig.danish.ui.noun.sampleNoun
import com.grig.danish.ui.noun.sampleNounWithAlt

private val correctColor = Color(0xFF4CAF50)
private val incorrectColor = Color(0xFFF44336)

@Composable
fun NounPracticeCard(
    noun: Noun,
    mode: LearnMode,
    userAnswer: String,
    answerResult: AnswerResult,
    onAnswerChanged: (String) -> Unit,
    onCheck: () -> Unit,
    onShowAnswer: () -> Unit,
    onSpeak: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val answered = answerResult != AnswerResult.UNANSWERED

    val glowColor by animateColorAsState(
        targetValue = when (answerResult) {
            AnswerResult.CORRECT -> correctColor
            AnswerResult.INCORRECT -> incorrectColor
            AnswerResult.UNANSWERED -> Color.Transparent
        },
        animationSpec = tween(durationMillis = 400),
        label = "glowColor"
    )

    val borderModifier = if (answered) {
        Modifier
            .shadow(
                elevation = 8.dp,
                shape = CardDefaults.shape,
                ambientColor = glowColor,
                spotColor = glowColor
            )
            .border(
                width = 2.dp,
                color = glowColor,
                shape = CardDefaults.shape
            )
    } else {
        Modifier
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(borderModifier),
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
                    HorizontalDivider()
                    if (answered) {
                        if (answerResult == AnswerResult.INCORRECT && userAnswer.isNotBlank()) {
                            IncorrectAnswerRow(userAnswer)
                        }
                        AnswerDanishForms(noun = noun, onSpeak = onSpeak)
                    } else {
                        AnswerInput(
                            userAnswer = userAnswer,
                            onAnswerChanged = onAnswerChanged,
                            onCheck = onCheck,
                            onShowAnswer = onShowAnswer,
                            placeholder = "Type Danish translation…",
                            hintLocales = LocaleList("da")
                        )
                    }
                }
                LearnMode.DK_TO_EN -> {
                    QuestionDanish(noun = noun, onSpeak = onSpeak)
                    HorizontalDivider()
                    if (answered) {
                        if (answerResult == AnswerResult.INCORRECT && userAnswer.isNotBlank()) {
                            IncorrectAnswerRow(userAnswer)
                        }
                        AnswerEnglishAndForms(noun = noun, onSpeak = onSpeak)
                    } else {
                        AnswerInput(
                            userAnswer = userAnswer,
                            onAnswerChanged = onAnswerChanged,
                            onCheck = onCheck,
                            onShowAnswer = onShowAnswer,
                            placeholder = "Type English translation…",
                            hintLocales = LocaleList("en")
                        )
                    }
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
private fun AnswerInput(
    userAnswer: String,
    onAnswerChanged: (String) -> Unit,
    onCheck: () -> Unit,
    onShowAnswer: () -> Unit,
    placeholder: String,
    hintLocales: LocaleList = LocaleList.current
) {
    OutlinedTextField(
        value = userAnswer,
        onValueChange = onAnswerChanged,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(placeholder) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            hintLocales = hintLocales
        ),
        keyboardActions = KeyboardActions(onDone = { onCheck() })
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onCheck,
            enabled = userAnswer.isNotBlank()
        ) {
            Text("Check")
        }
        OutlinedButton(onClick = onShowAnswer) {
            Text("Show answer")
        }
    }
}

@Composable
private fun IncorrectAnswerRow(userAnswer: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = "Your answer",
            style = MaterialTheme.typography.labelMedium,
            color = incorrectColor
        )
        Text(
            text = userAnswer,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            color = incorrectColor,
            textDecoration = TextDecoration.LineThrough
        )
    }
}

@Composable
private fun AnswerDanishForms(noun: Noun, onSpeak: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        DanishFormRow(label = "Danish", value = noun.danish, onSpeak = onSpeak)
        DanishFormRow(label = "The form", value = noun.theForm, onSpeak = onSpeak)
        DanishFormRow(label = "Plural", value = noun.plural, onSpeak = onSpeak)
        DanishFormRow(label = "The plural", value = noun.thePlural, onSpeak = onSpeak)

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

@Composable
private fun AnswerEnglishAndForms(noun: Noun, onSpeak: (String) -> Unit) {
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

        DanishFormRow(label = "The form", value = noun.theForm, onSpeak = onSpeak)
        DanishFormRow(label = "Plural", value = noun.plural, onSpeak = onSpeak)
        DanishFormRow(label = "The plural", value = noun.thePlural, onSpeak = onSpeak)

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


@Preview(name = "EN→DK Unanswered")
@Composable
private fun PracticeCardEnToDkUnansweredPreview() {
    DanishTheme(darkTheme = false) {
        NounPracticeCard(
            noun = sampleNoun,
            mode = LearnMode.EN_TO_DK,
            userAnswer = "",
            answerResult = AnswerResult.UNANSWERED,
            onAnswerChanged = {},
            onCheck = {},
            onShowAnswer = {},
            onSpeak = {}
        )
    }
}

@Preview(name = "EN→DK Correct")
@Composable
private fun PracticeCardEnToDkCorrectPreview() {
    DanishTheme(darkTheme = false) {
        NounPracticeCard(
            noun = sampleNoun,
            mode = LearnMode.EN_TO_DK,
            userAnswer = "en bog",
            answerResult = AnswerResult.CORRECT,
            onAnswerChanged = {},
            onCheck = {},
            onShowAnswer = {},
            onSpeak = {}
        )
    }
}

@Preview(name = "EN→DK Incorrect")
@Composable
private fun PracticeCardEnToDkIncorrectPreview() {
    DanishTheme(darkTheme = false) {
        NounPracticeCard(
            noun = sampleNounWithAlt,
            mode = LearnMode.EN_TO_DK,
            userAnswer = "et bog",
            answerResult = AnswerResult.INCORRECT,
            onAnswerChanged = {},
            onCheck = {},
            onShowAnswer = {},
            onSpeak = {}
        )
    }
}

@Preview(name = "DK→EN Unanswered")
@Composable
private fun PracticeCardDkToEnUnansweredPreview() {
    DanishTheme(darkTheme = false) {
        NounPracticeCard(
            noun = sampleNoun,
            mode = LearnMode.DK_TO_EN,
            userAnswer = "",
            answerResult = AnswerResult.UNANSWERED,
            onAnswerChanged = {},
            onCheck = {},
            onShowAnswer = {},
            onSpeak = {}
        )
    }
}

@Preview(name = "DK→EN Correct")
@Composable
private fun PracticeCardDkToEnCorrectPreview() {
    DanishTheme(darkTheme = false) {
        NounPracticeCard(
            noun = sampleNounWithAlt,
            mode = LearnMode.DK_TO_EN,
            userAnswer = "clock",
            answerResult = AnswerResult.CORRECT,
            onAnswerChanged = {},
            onCheck = {},
            onShowAnswer = {},
            onSpeak = {}
        )
    }
}

@Preview(name = "DK→EN Incorrect Dark")
@Composable
private fun PracticeCardDkToEnIncorrectDarkPreview() {
    DanishTheme(darkTheme = true) {
        NounPracticeCard(
            noun = sampleNounWithAlt,
            mode = LearnMode.DK_TO_EN,
            userAnswer = "wrong",
            answerResult = AnswerResult.INCORRECT,
            onAnswerChanged = {},
            onCheck = {},
            onShowAnswer = {},
            onSpeak = {}
        )
    }
}

@Preview(name = "EN→DK Unanswered Dark")
@Composable
private fun PracticeCardEnToDkUnansweredDarkPreview() {
    DanishTheme(darkTheme = true) {
        NounPracticeCard(
            noun = sampleNoun,
            mode = LearnMode.EN_TO_DK,
            userAnswer = "en b",
            answerResult = AnswerResult.UNANSWERED,
            onAnswerChanged = {},
            onCheck = {},
            onShowAnswer = {},
            onSpeak = {}
        )
    }
}

@Preview(name = "EN→DK Correct Dark")
@Composable
private fun PracticeCardEnToDkCorrectDarkPreview() {
    DanishTheme(darkTheme = true) {
        NounPracticeCard(
            noun = sampleNoun,
            mode = LearnMode.EN_TO_DK,
            userAnswer = "en bog",
            answerResult = AnswerResult.CORRECT,
            onAnswerChanged = {},
            onCheck = {},
            onShowAnswer = {},
            onSpeak = {}
        )
    }
}
