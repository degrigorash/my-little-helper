package com.grig.myanimelist.ui.persondetail

import com.grig.myanimelist.data.model.jikan.PersonDetail

sealed interface PersonDetailState {
    /**
     * @param progress fraction in 0f..1f of the favorites lookups completed, or null while
     * the total is still unknown (i.e. before the profile/voice list has loaded).
     */
    data class Loading(val progress: Float? = null) : PersonDetailState
    data class Content(val person: PersonDetail) : PersonDetailState
    data class Error(val message: String) : PersonDetailState
}
