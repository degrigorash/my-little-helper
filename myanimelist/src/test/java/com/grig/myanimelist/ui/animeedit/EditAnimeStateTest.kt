package com.grig.myanimelist.ui.animeedit

import com.grig.myanimelist.data.model.anime.MalAnimeListStatus
import com.grig.myanimelist.data.model.anime.MalAnimeWatchingStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test

class EditAnimeStateTest {

    @Test
    fun `from maps MalAnimeListStatus correctly`() {
        val listStatus = MalAnimeListStatus(
            status = MalAnimeWatchingStatus.Watching,
            score = 8,
            numEpisodesWatched = 12,
            finishDate = "2024-06-15"
        )

        val state = EditAnimeState.from(listStatus)

        assertEquals(MalAnimeWatchingStatus.Watching, state.status)
        assertEquals(8, state.score)
        assertEquals(12, state.numEpisodesWatched)
        assertEquals("2024-06-15", state.finishDate)
        assertFalse(state.isSaving)
        assertNull(state.error)
    }

    @Test
    fun `from null returns sensible defaults`() {
        val state = EditAnimeState.from(null)

        assertEquals(MalAnimeWatchingStatus.PlanToWatch, state.status)
        assertEquals(0, state.score)
        assertEquals(0, state.numEpisodesWatched)
        assertNull(state.finishDate)
        assertFalse(state.isSaving)
        assertNull(state.error)
    }

    @Test
    fun `from maps partial status with defaults for null fields`() {
        val listStatus = MalAnimeListStatus(
            status = MalAnimeWatchingStatus.Completed,
            score = null,
            numEpisodesWatched = null,
            finishDate = null
        )

        val state = EditAnimeState.from(listStatus)

        assertEquals(MalAnimeWatchingStatus.Completed, state.status)
        assertEquals(0, state.score)
        assertEquals(0, state.numEpisodesWatched)
        assertNull(state.finishDate)
    }
}
