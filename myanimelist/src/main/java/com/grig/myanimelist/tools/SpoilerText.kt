package com.grig.myanimelist.tools

/**
 * A contiguous run of text from a MAL/Jikan "about"/synopsis field, tagged as either
 * plain prose or a spoiler passage.
 *
 * MAL embeds spoilers inline with BBCode-style markup, e.g.
 * `He is actually [spoiler]the villain's son[/spoiler].` or the labelled variant
 * `[spoiler=Ending]...[/spoiler]`. Rendering the raw string shows those tags verbatim
 * and reveals the spoiler, so callers should parse with [parseSpoilerText] and hide the
 * [SpoilerSegment.Spoiler] runs behind a tap-to-reveal affordance.
 */
sealed interface SpoilerSegment {
    val text: String

    data class Plain(override val text: String) : SpoilerSegment

    data class Spoiler(override val text: String) : SpoilerSegment
}

private val SPOILER_REGEX =
    Regex("""\[spoiler(?:=[^\]]*)?]([\s\S]*?)\[/spoiler]""", RegexOption.IGNORE_CASE)

/**
 * Splits [raw] into ordered [SpoilerSegment]s on `[spoiler]…[/spoiler]` markup
 * (including the labelled `[spoiler=label]…[/spoiler]` form). Text outside spoiler tags
 * becomes [SpoilerSegment.Plain]; text inside becomes [SpoilerSegment.Spoiler].
 *
 * Empty runs are dropped, and a string with no spoiler markup yields a single Plain
 * segment (or an empty list when blank). The result never contains the literal tags.
 */
fun parseSpoilerText(raw: String?): List<SpoilerSegment> {
    if (raw.isNullOrEmpty()) return emptyList()

    val segments = mutableListOf<SpoilerSegment>()
    var cursor = 0

    for (match in SPOILER_REGEX.findAll(raw)) {
        if (match.range.first > cursor) {
            val plain = raw.substring(cursor, match.range.first)
            if (plain.isNotEmpty()) segments += SpoilerSegment.Plain(plain)
        }
        val inner = match.groupValues[1]
        if (inner.isNotBlank()) segments += SpoilerSegment.Spoiler(inner.trim())
        cursor = match.range.last + 1
    }

    if (cursor < raw.length) {
        val tail = raw.substring(cursor)
        if (tail.isNotEmpty()) segments += SpoilerSegment.Plain(tail)
    }

    return segments
}

/** True if [raw] contains at least one `[spoiler]` passage. */
fun hasSpoilers(raw: String?): Boolean =
    raw != null && SPOILER_REGEX.containsMatchIn(raw)
