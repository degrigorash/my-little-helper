package com.grig.danish.ui.noun

import com.grig.danish.data.model.Noun

val sampleNoun = Noun(
    id = 1,
    english = "book",
    alt = emptyList(),
    danish = "en bog",
    theForm = "bogen",
    plural = "bøger",
    thePlural = "bøgerne",
    folder = "default",
    notes = null
)

val sampleNounWithAlt = Noun(
    id = 7,
    english = "clock",
    alt = listOf("watch"),
    danish = "et ur",
    theForm = "uret",
    plural = "ure",
    thePlural = "urene",
    folder = "default",
    notes = "Can also mean wristwatch"
)
