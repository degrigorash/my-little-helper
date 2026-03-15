package com.grig.myanimelist.ui.mangaedit

import com.grig.myanimelist.data.model.manga.MalMangaListStatus
import com.grig.myanimelist.data.model.manga.MalMangaReadingStatus

data class EditMangaState(
    val status: MalMangaReadingStatus = MalMangaReadingStatus.PlanToRead,
    val score: Int = 0,
    val numChaptersRead: Int = 0,
    val numVolumesRead: Int = 0,
    val finishDate: String? = null,
    val isSaving: Boolean = false,
    val error: String? = null
) {
    companion object {
        fun from(listStatus: MalMangaListStatus?): EditMangaState = EditMangaState(
            status = listStatus?.status ?: MalMangaReadingStatus.PlanToRead,
            score = listStatus?.score ?: 0,
            numChaptersRead = listStatus?.numChaptersRead ?: 0,
            numVolumesRead = listStatus?.numVolumesRead ?: 0,
            finishDate = listStatus?.finishDate
        )
    }
}

sealed interface EditMangaEvent {
    data class Saved(val updatedStatus: MalMangaListStatus) : EditMangaEvent
    data object Deleted : EditMangaEvent
    data class Error(val message: String) : EditMangaEvent
}
