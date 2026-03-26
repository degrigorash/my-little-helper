package com.grig.myanimelist.ui.reviews

import com.grig.myanimelist.data.model.jikan.JikanReview
import com.grig.myanimelist.data.model.jikan.JikanReviewReactions
import com.grig.myanimelist.data.model.jikan.JikanReviewUser

val previewReview = JikanReview(
    malId = 1,
    date = "2025-06-15T00:00:00+00:00",
    review = "This anime is an absolute masterpiece. The animation quality is breathtaking and the story keeps you on the edge of your seat. Highly recommended for anyone who loves action and drama.",
    score = 9,
    tags = listOf("Recommended"),
    isSpoiler = false,
    isPreliminary = false,
    reactions = JikanReviewReactions(overall = 42, nice = 10, loveIt = 15),
    user = JikanReviewUser(username = "AnimeReviewer42")
)

val previewReviewLow = JikanReview(
    malId = 2,
    date = "2025-05-20T00:00:00+00:00",
    review = "Disappointing sequel that fails to live up to the original. The pacing is off and character development is lacking.",
    score = 4,
    tags = listOf("Not Recommended"),
    isSpoiler = false,
    isPreliminary = true,
    reactions = JikanReviewReactions(overall = 8),
    user = JikanReviewUser(username = "CriticalViewer")
)

val previewReviewMixed = JikanReview(
    malId = 3,
    date = "2025-04-10T00:00:00+00:00",
    review = "It has its moments but overall the show is inconsistent. Some arcs are brilliant while others feel like filler.",
    score = 6,
    tags = listOf("Mixed Feelings"),
    isSpoiler = false,
    isPreliminary = false,
    reactions = JikanReviewReactions(overall = 15, nice = 5),
    user = JikanReviewUser(username = "BalancedTake")
)

val previewReviews = listOf(previewReview, previewReviewLow, previewReviewMixed)
