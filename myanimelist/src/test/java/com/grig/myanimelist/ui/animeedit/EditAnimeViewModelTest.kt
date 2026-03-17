package com.grig.myanimelist.ui.animeedit

import app.cash.turbine.test
import com.grig.myanimelist.MainDispatcherRule
import com.grig.myanimelist.data.MalRepository
import com.grig.myanimelist.data.model.anime.MalAnime
import com.grig.myanimelist.data.model.anime.MalAnimeListStatus
import com.grig.myanimelist.data.model.anime.MalAnimeWatchingStatus
import com.grig.myanimelist.ui.home.AnimeCardData
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class EditAnimeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val malRepository: MalRepository = mockk(relaxed = true)

    private fun createViewModel() = EditAnimeViewModel(malRepository)

    @Test
    fun `init populates state from AnimeCardData`() {
        val viewModel = createViewModel()
        val data = AnimeCardData(
            anime = MalAnime(id = 1, title = "Test"),
            listStatus = MalAnimeListStatus(
                status = MalAnimeWatchingStatus.Watching,
                score = 8,
                numEpisodesWatched = 5,
                finishDate = "2024-06-15"
            )
        )

        viewModel.init(data)

        val state = viewModel.state.value
        assertEquals(MalAnimeWatchingStatus.Watching, state.status)
        assertEquals(8, state.score)
        assertEquals(5, state.numEpisodesWatched)
        assertEquals("2024-06-15", state.finishDate)
    }

    @Test
    fun `init with null listStatus uses defaults`() {
        val viewModel = createViewModel()
        val data = AnimeCardData(
            anime = MalAnime(id = 1, title = "Test"),
            listStatus = null
        )

        viewModel.init(data)

        val state = viewModel.state.value
        assertEquals(MalAnimeWatchingStatus.PlanToWatch, state.status)
        assertEquals(0, state.score)
        assertEquals(0, state.numEpisodesWatched)
        assertNull(state.finishDate)
    }

    @Test
    fun `setScore clamps above max to 10`() {
        val viewModel = createViewModel()

        viewModel.setScore(15)

        assertEquals(10, viewModel.state.value.score)
    }

    @Test
    fun `setScore clamps below min to 0`() {
        val viewModel = createViewModel()

        viewModel.setScore(-5)

        assertEquals(0, viewModel.state.value.score)
    }

    @Test
    fun `setScore accepts valid value`() {
        val viewModel = createViewModel()

        viewModel.setScore(7)

        assertEquals(7, viewModel.state.value.score)
    }

    @Test
    fun `setEpisodes clamps negative to 0`() {
        val viewModel = createViewModel()

        viewModel.setEpisodes(-3)

        assertEquals(0, viewModel.state.value.numEpisodesWatched)
    }

    @Test
    fun `setEpisodes accepts valid value`() {
        val viewModel = createViewModel()

        viewModel.setEpisodes(12)

        assertEquals(12, viewModel.state.value.numEpisodesWatched)
    }

    @Test
    fun `save emits Saved event on success`() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = createViewModel()
        val updatedStatus = MalAnimeListStatus(status = MalAnimeWatchingStatus.Completed, score = 9)
        coEvery { malRepository.updateAnimeListStatus(any(), any(), any(), any(), any()) } returns
            Result.success(updatedStatus)

        viewModel.events.test {
            viewModel.save(1)
            val event = awaitItem()
            assertTrue(event is EditAnimeEvent.Saved)
            assertEquals(updatedStatus, (event as EditAnimeEvent.Saved).updatedStatus)
        }

        assertFalse(viewModel.state.value.isSaving)
    }

    @Test
    fun `save emits Error event on failure`() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = createViewModel()
        coEvery { malRepository.updateAnimeListStatus(any(), any(), any(), any(), any()) } returns
            Result.failure(RuntimeException("Save failed"))

        viewModel.events.test {
            viewModel.save(1)
            val event = awaitItem()
            assertTrue(event is EditAnimeEvent.Error)
            assertEquals("Save failed", (event as EditAnimeEvent.Error).message)
        }

        assertFalse(viewModel.state.value.isSaving)
        assertEquals("Save failed", viewModel.state.value.error)
    }

    @Test
    fun `delete emits Deleted event on success`() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = createViewModel()
        coEvery { malRepository.deleteAnimeListItem(any()) } returns Result.success(Unit)

        viewModel.events.test {
            viewModel.delete(1)
            val event = awaitItem()
            assertTrue(event is EditAnimeEvent.Deleted)
        }

        assertFalse(viewModel.state.value.isSaving)
    }

    @Test
    fun `delete emits Error event on failure`() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = createViewModel()
        coEvery { malRepository.deleteAnimeListItem(any()) } returns
            Result.failure(RuntimeException("Delete failed"))

        viewModel.events.test {
            viewModel.delete(1)
            val event = awaitItem()
            assertTrue(event is EditAnimeEvent.Error)
            assertEquals("Delete failed", (event as EditAnimeEvent.Error).message)
        }
    }
}
