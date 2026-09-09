package com.grig.myanimelist.ui.common

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import com.grig.core.theme.AppTheme
import com.grig.myanimelist.tools.SpoilerSegment
import com.grig.myanimelist.tools.parseSpoilerText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

private const val SPOILER_TAG = "spoiler"

/**
 * Renders a MAL/Jikan "about"/synopsis string that may contain inline
 * `[spoiler]…[/spoiler]` passages. Plain prose shows normally; each spoiler passage is
 * redacted (drawn as an opaque block over its own hidden text) until the reader taps it,
 * which reveals just that passage. Tapping again re-hides it.
 *
 * The redaction is a solid fill rather than a blur so it hides reliably on every API
 * level (Modifier.blur is a no-op below API 31). Spoilers reflow with the surrounding
 * text, so the description still reads as one continuous block.
 */
@Composable
fun SpoilerText(
    text: String,
    modifier: Modifier = Modifier,
    style: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color = MaterialTheme.colorScheme.onSurface,
    fontSize: TextUnit = TextUnit.Unspecified
) {
    val segments = remember(text) { parseSpoilerText(text) }
    // Index into `segments` of every spoiler the reader has revealed.
    val revealed = remember(text) { mutableStateOf(emptySet<Int>()) }
    var layout by remember { mutableStateOf<TextLayoutResult?>(null) }

    val redactionColor = MaterialTheme.colorScheme.onSurface
    val revealedSpoilerColor = MaterialTheme.colorScheme.error

    val annotated: AnnotatedString = buildAnnotatedString {
        segments.forEachIndexed { index, segment ->
            when (segment) {
                is SpoilerSegment.Plain -> append(segment.text)
                is SpoilerSegment.Spoiler -> {
                    val isRevealed = index in revealed.value
                    pushStringAnnotation(tag = SPOILER_TAG, annotation = index.toString())
                    if (isRevealed) {
                        withStyle(SpanStyle(color = revealedSpoilerColor)) {
                            append(segment.text)
                        }
                    } else {
                        // Same glyphs, but painted over themselves so nothing shows
                        // through the block; the reader sees a solid redaction bar.
                        withStyle(
                            SpanStyle(color = redactionColor, background = redactionColor)
                        ) {
                            append(segment.text)
                        }
                    }
                    pop()
                }
            }
        }
    }

    Text(
        text = annotated,
        style = style,
        color = color,
        fontSize = fontSize,
        onTextLayout = { layout = it },
        modifier = modifier.pointerInput(segments) {
            detectTapGestures { offset ->
                val result = layout ?: return@detectTapGestures
                val position = result.getOffsetForPosition(offset)
                annotated.getStringAnnotations(SPOILER_TAG, position, position)
                    .firstOrNull()
                    ?.let { annotation ->
                        val index = annotation.item.toIntOrNull() ?: return@let
                        revealed.value = revealed.value.toMutableSet().apply {
                            if (!add(index)) remove(index)
                        }
                    }
            }
        }
    )
}

@Preview(name = "Spoiler Text - Hidden", showBackground = true)
@Composable
private fun SpoilerTextPreview() {
    AppTheme(darkTheme = false) {
        SpoilerText(
            text = "He is a cheerful pirate who [spoiler]turns out to be the son of a " +
                "revolutionary[/spoiler] and dreams of becoming the Pirate King."
        )
    }
}

@Preview(name = "Spoiler Text - Dark", showBackground = true)
@Composable
private fun SpoilerTextDarkPreview() {
    AppTheme(darkTheme = true) {
        SpoilerText(
            text = "He is a cheerful pirate who [spoiler]turns out to be the son of a " +
                "revolutionary[/spoiler] and dreams of becoming the Pirate King."
        )
    }
}

@Preview(name = "Spoiler Text - No Spoilers", showBackground = true)
@Composable
private fun SpoilerTextPlainPreview() {
    AppTheme(darkTheme = false) {
        Column {
            SpoilerText(text = "A plain description with no spoiler markup at all.")
        }
    }
}
