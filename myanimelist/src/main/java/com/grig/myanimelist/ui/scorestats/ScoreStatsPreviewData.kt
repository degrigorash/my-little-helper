package com.grig.myanimelist.ui.scorestats

import com.grig.myanimelist.data.model.anime.MalAnimeWatchingStatus
import com.grig.myanimelist.data.model.jikan.JikanScoreBucket
import com.grig.myanimelist.ui.home.watchingStatusColor

// Real Chiikawa (MAL 50250) numbers: a heavily top-weighted distribution.
val previewScoreBuckets = listOf(
    JikanScoreBucket(score = 10, votes = 4112, percentage = 50.5f),
    JikanScoreBucket(score = 9, votes = 1180, percentage = 14.5f),
    JikanScoreBucket(score = 8, votes = 1212, percentage = 14.9f),
    JikanScoreBucket(score = 7, votes = 776, percentage = 9.5f),
    JikanScoreBucket(score = 6, votes = 347, percentage = 4.3f),
    JikanScoreBucket(score = 5, votes = 198, percentage = 2.4f),
    JikanScoreBucket(score = 4, votes = 82, percentage = 1f),
    JikanScoreBucket(score = 3, votes = 50, percentage = 0.6f),
    JikanScoreBucket(score = 2, votes = 31, percentage = 0.4f),
    JikanScoreBucket(score = 1, votes = 149, percentage = 1.8f)
)

val previewTotalVotes = previewScoreBuckets.sumOf { it.votes }

val previewStatusSlices = listOf(
    MalAnimeWatchingStatus.Watching to 14_711,
    MalAnimeWatchingStatus.Completed to 3,
    MalAnimeWatchingStatus.OnHold to 1_962,
    MalAnimeWatchingStatus.Dropped to 1_245,
    MalAnimeWatchingStatus.PlanToWatch to 8_479
).map { (status, count) -> StatusSlice(status.displayName, count, watchingStatusColor(status)) }

val previewTotalMembers = previewStatusSlices.sumOf { it.count }
