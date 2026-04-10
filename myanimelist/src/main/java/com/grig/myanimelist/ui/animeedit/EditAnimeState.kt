package com.grig.myanimelist.ui.animeedit

import com.grig.myanimelist.data.model.anime.MalAnimeListStatus
import com.grig.myanimelist.data.model.anime.MalAnimeWatchingStatus

data class EditAnimeState(
    val status: MalAnimeWatchingStatus = MalAnimeWatchingStatus.PlanToWatch,
    val score: Int = 0,
    val numEpisodesWatched: Int = 0,
    val finishDate: String? = null,
    val isSaving: Boolean = false,
    val error: String? = null,
    val isBookmarked: Boolean = false,
    val bookmarkNotes: String = "",
    val showBookmarkEditor: Boolean = false
) {
    companion object {
        fun from(listStatus: MalAnimeListStatus?): EditAnimeState = EditAnimeState(
            status = listStatus?.status ?: MalAnimeWatchingStatus.PlanToWatch,
            score = listStatus?.score ?: 0,
            numEpisodesWatched = listStatus?.numEpisodesWatched ?: 0,
            finishDate = listStatus?.finishDate,
            bookmarkNotes = listStatus?.comments ?: ""
        )
    }
}

sealed interface EditAnimeEvent {
    data class Saved(val updatedStatus: MalAnimeListStatus) : EditAnimeEvent
    data object Deleted : EditAnimeEvent
    data class Error(val message: String) : EditAnimeEvent
}
