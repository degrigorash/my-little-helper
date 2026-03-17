package com.grig.myanimelist.ui.animesearch

import com.grig.myanimelist.MainDispatcherRule
import com.grig.myanimelist.data.MalRepository
import com.grig.myanimelist.data.model.MalUserState
import com.grig.myanimelist.data.model.anime.MalAnime
import com.grig.myanimelist.data.model.anime.MalAnimeList
import com.grig.myanimelist.data.model.anime.MalAnimeListStatus
import com.grig.myanimelist.data.model.anime.MalAnimeNode
import com.grig.myanimelist.data.model.anime.MalAnimeWatchingStatus
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AnimeSearchViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val malRepository: MalRepository = mockk(relaxed = true)

    private fun createViewModel() = AnimeSearchViewModel(malRepository)

    @Test
    fun `onQueryChange clears results when query is shorter than 3 chars`() {
        val viewModel = createViewModel()

        viewModel.onQueryChange("ab")

        val state = viewModel.state.value
        assertEquals("ab", state.query)
        assertTrue(state.searchResults.isEmpty())
        assertFalse(state.isSearching)
    }

    @Test
    fun `onQueryChange triggers search after debounce for query ge 3 chars`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val animeList = MalAnimeList(
                data = listOf(MalAnimeNode(anime = MalAnime(id = 1, title = "Test Anime")))
            )
            coEvery { malRepository.searchAnime("test query") } returns Result.success(animeList)

            val viewModel = createViewModel()
            viewModel.onQueryChange("test query")

            assertTrue(viewModel.state.value.isSearching)

            advanceUntilIdle()

            assertFalse(viewModel.state.value.isSearching)
            assertEquals(1, viewModel.state.value.searchResults.size)
            assertEquals("Test Anime", viewModel.state.value.searchResults[0].title)
        }

    @Test
    fun `onQueryChange clears selectedAnime`() {
        val viewModel = createViewModel()

        viewModel.onQueryChange("new query")

        assertNull(viewModel.state.value.selectedAnime)
    }

    @Test
    fun `addToMyList updates state after successful add`() = runTest(mainDispatcherRule.testDispatcher) {
        val anime = MalAnime(id = 1, title = "Test Anime")
        val animeWithStatus = anime.copy(
            myListStatus = MalAnimeListStatus(status = MalAnimeWatchingStatus.PlanToWatch)
        )
        coEvery { malRepository.getAnimeDetails(1) } returnsMany listOf(
            Result.success(anime),
            Result.success(animeWithStatus)
        )
        coEvery { malRepository.updateAnimeListStatus(any(), any(), any(), any(), any()) } returns
            Result.success(MalAnimeListStatus(status = MalAnimeWatchingStatus.PlanToWatch))

        val viewModel = createViewModel()
        viewModel.onAnimeSelected(1)
        advanceUntilIdle()

        assertNotNull(viewModel.state.value.selectedAnime)
        assertFalse(viewModel.state.value.isInMyList)

        viewModel.addToMyList()
        advanceUntilIdle()

        assertTrue(viewModel.state.value.isInMyList)
        assertFalse(viewModel.state.value.isUpdatingList)
    }

    @Test
    fun `deleteFromMyList updates state after successful delete`() = runTest(mainDispatcherRule.testDispatcher) {
        val animeInList = MalAnime(
            id = 1,
            title = "Test Anime",
            myListStatus = MalAnimeListStatus(status = MalAnimeWatchingStatus.PlanToWatch)
        )
        val animeNotInList = MalAnime(id = 1, title = "Test Anime")
        coEvery { malRepository.getAnimeDetails(1) } returnsMany listOf(
            Result.success(animeInList),
            Result.success(animeNotInList)
        )
        coEvery { malRepository.deleteAnimeListItem(1) } returns Result.success(Unit)

        val viewModel = createViewModel()
        viewModel.onAnimeSelected(1)
        advanceUntilIdle()

        assertTrue(viewModel.state.value.isInMyList)

        viewModel.deleteFromMyList()
        advanceUntilIdle()

        assertFalse(viewModel.state.value.isInMyList)
        assertFalse(viewModel.state.value.isUpdatingList)
    }

    @Test
    fun `isAuthorized returns true when user is authorized`() = runTest(mainDispatcherRule.testDispatcher) {
        val user = com.grig.myanimelist.data.model.MalUser(id = 1, name = "Test", joinedAt = "2020-01-01")
        every { malRepository.userFlow } returns flowOf(MalUserState.Authorized(user))

        val viewModel = createViewModel()
        assertTrue(viewModel.isAuthorized())
    }

    @Test
    fun `isAuthorized returns false when user is unauthorized`() = runTest(mainDispatcherRule.testDispatcher) {
        every { malRepository.userFlow } returns flowOf(MalUserState.Unauthorized)

        val viewModel = createViewModel()
        assertFalse(viewModel.isAuthorized())
    }
}
