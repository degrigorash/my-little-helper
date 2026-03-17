package com.grig.myanimelist.ui.home

import com.grig.myanimelist.MainDispatcherRule
import com.grig.myanimelist.data.FilterPreferencesManager
import com.grig.myanimelist.data.MalRepository
import com.grig.myanimelist.data.model.MalUser
import com.grig.myanimelist.data.model.MalUserState
import com.grig.myanimelist.data.model.anime.MalAnime
import com.grig.myanimelist.data.model.anime.MalAnimeList
import com.grig.myanimelist.data.model.anime.MalAnimeListStatus
import com.grig.myanimelist.data.model.anime.MalAnimeNode
import com.grig.myanimelist.data.model.anime.MalAnimeWatchingStatus
import com.grig.myanimelist.data.model.manga.MalMangaList
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MalHomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val malRepository: MalRepository = mockk(relaxed = true)
    private val filterPreferences: FilterPreferencesManager = mockk()

    private val testUser = MalUser(id = 1, name = "TestUser", joinedAt = "2020-01-01")

    @Before
    fun setUp() {
        every { malRepository.userFlow } returns flowOf(MalUserState.Unauthorized)
        coEvery { filterPreferences.loadAnimeFilter() } returns emptySet()
        coEvery { filterPreferences.loadMangaFilter() } returns emptySet()
        coEvery { filterPreferences.saveAnimeFilter(any()) } just Runs
        coEvery { filterPreferences.saveMangaFilter(any()) } just Runs
    }

    private fun createViewModel() = MalHomeViewModel(malRepository, filterPreferences)

    @Test
    fun `selectTab switches to manga and triggers reload`() = runTest(mainDispatcherRule.testDispatcher) {
        every { malRepository.userFlow } returns flowOf(MalUserState.Authorized(testUser))
        coEvery { malRepository.getUserAnimeList(any(), any(), any()) } returns Result.success(MalAnimeList(emptyList()))
        coEvery { malRepository.getUserMangaList(any(), any(), any()) } returns Result.success(MalMangaList(emptyList()))

        val viewModel = createViewModel()
        advanceUntilIdle()
        assertEquals(MalTab.Anime, viewModel.activeTab.value)

        viewModel.selectTab(MalTab.Manga)
        advanceUntilIdle()

        assertEquals(MalTab.Manga, viewModel.activeTab.value)
        coVerify { malRepository.getUserMangaList(any(), any(), any()) }
    }

    @Test
    fun `selectAnimeFilter toggles filter on and persists`() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.selectAnimeFilter(MalAnimeWatchingStatus.Watching)
        advanceUntilIdle()

        assertEquals(setOf(MalAnimeWatchingStatus.Watching), viewModel.animeFilter.value)
        coVerify { filterPreferences.saveAnimeFilter(setOf(MalAnimeWatchingStatus.Watching)) }
    }

    @Test
    fun `selectAnimeFilter toggles filter off when already selected`() = runTest(mainDispatcherRule.testDispatcher) {
        coEvery { filterPreferences.loadAnimeFilter() } returns setOf(MalAnimeWatchingStatus.Watching)
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.selectAnimeFilter(MalAnimeWatchingStatus.Watching)
        advanceUntilIdle()

        assertTrue(viewModel.animeFilter.value.isEmpty())
        coVerify { filterPreferences.saveAnimeFilter(emptySet()) }
    }

    @Test
    fun `applyAnimeFilter filters cached list and sorts by mean score`() = runTest(mainDispatcherRule.testDispatcher) {
        val anime1 = MalAnime(id = 1, title = "Low Score", mean = 7.5f)
        val anime2 = MalAnime(id = 2, title = "Completed", mean = 9.0f)
        val anime3 = MalAnime(id = 3, title = "High Score", mean = 8.0f)

        val animeList = MalAnimeList(
            data = listOf(
                MalAnimeNode(anime = anime1, listStatus = MalAnimeListStatus(status = MalAnimeWatchingStatus.Watching)),
                MalAnimeNode(anime = anime2, listStatus = MalAnimeListStatus(status = MalAnimeWatchingStatus.Completed)),
                MalAnimeNode(anime = anime3, listStatus = MalAnimeListStatus(status = MalAnimeWatchingStatus.Watching))
            )
        )

        every { malRepository.userFlow } returns flowOf(MalUserState.Authorized(testUser))
        coEvery { malRepository.getUserAnimeList(any(), any(), any()) } returns Result.success(animeList)

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.selectAnimeFilter(MalAnimeWatchingStatus.Watching)
        advanceUntilIdle()

        val state = viewModel.listState.value
        assertTrue(state is ListState.AnimeContent)
        val content = state as ListState.AnimeContent
        assertEquals(2, content.animes.size)
        assertEquals(3, content.animes[0].anime.id) // mean 8.0
        assertEquals(1, content.animes[1].anime.id) // mean 7.5
    }

    @Test
    fun `onAnimeUpdated updates cached data and reapplies filter`() = runTest(mainDispatcherRule.testDispatcher) {
        val anime = MalAnime(id = 1, title = "Test Anime", mean = 7.5f)
        val animeList = MalAnimeList(
            data = listOf(
                MalAnimeNode(anime = anime, listStatus = MalAnimeListStatus(status = MalAnimeWatchingStatus.Watching, score = 7))
            )
        )

        every { malRepository.userFlow } returns flowOf(MalUserState.Authorized(testUser))
        coEvery { malRepository.getUserAnimeList(any(), any(), any()) } returns Result.success(animeList)

        val viewModel = createViewModel()
        advanceUntilIdle()

        val newStatus = MalAnimeListStatus(status = MalAnimeWatchingStatus.Completed, score = 10)
        viewModel.onAnimeUpdated(1, newStatus)

        val state = viewModel.listState.value as ListState.AnimeContent
        assertEquals(MalAnimeWatchingStatus.Completed, state.animes[0].listStatus?.status)
        assertEquals(10, state.animes[0].listStatus?.score)
        assertNull(viewModel.editSheetAnime.value)
    }

    @Test
    fun `onAnimeDeleted removes item from cache`() = runTest(mainDispatcherRule.testDispatcher) {
        val anime1 = MalAnime(id = 1, title = "Anime A", mean = 7.5f)
        val anime2 = MalAnime(id = 2, title = "Anime B", mean = 8.0f)
        val animeList = MalAnimeList(
            data = listOf(
                MalAnimeNode(anime = anime1, listStatus = MalAnimeListStatus()),
                MalAnimeNode(anime = anime2, listStatus = MalAnimeListStatus())
            )
        )

        every { malRepository.userFlow } returns flowOf(MalUserState.Authorized(testUser))
        coEvery { malRepository.getUserAnimeList(any(), any(), any()) } returns Result.success(animeList)

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onAnimeDeleted(1)

        val state = viewModel.listState.value as ListState.AnimeContent
        assertEquals(1, state.animes.size)
        assertEquals(2, state.animes[0].anime.id)
    }
}
