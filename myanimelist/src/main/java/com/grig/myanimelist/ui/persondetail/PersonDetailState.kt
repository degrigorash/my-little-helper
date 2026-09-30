package com.grig.myanimelist.ui.persondetail

import com.grig.myanimelist.data.model.jikan.PersonDetail

sealed interface PersonDetailState {
    /**
     * @param progress fraction in 0f..1f of the favorites lookups completed, or null while
     * the total is still unknown (i.e. before the profile/voice list has loaded).
     * @param isThrottled true while MAL has stopped responding and the lookups are paused
     * until it recovers.
     */
    data class Loading(
        val progress: Float? = null,
        val isThrottled: Boolean = false
    ) : PersonDetailState
    data class Content(val person: PersonDetail) : PersonDetailState

    /**
     * The person voices no characters but has staff or author credits (a mangaka opened via a
     * myanimelist.net/people link), which the author screen shows instead.
     */
    data class NoVoiceRoles(val personId: Int) : PersonDetailState
    data class Error(val message: String) : PersonDetailState
}
