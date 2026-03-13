package com.grig.myanimelist.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grig.myanimelist.data.MalRepository
import com.grig.myanimelist.data.model.MalUserState
import com.grig.myanimelist.data.model.anime.MalAnime
import com.grig.myanimelist.data.model.anime.MalAnimeWatchingStatus
import com.grig.myanimelist.data.model.manga.MalManga
import com.grig.myanimelist.data.model.manga.MalMangaReadingStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MalHomeViewModel @Inject constructor(
    private val malRepository: MalRepository
) : ViewModel() {

    val malUserFlow = malRepository.userFlow

    private val _activeTab = MutableStateFlow(MalTab.Anime)
    val activeTab: StateFlow<MalTab> = _activeTab.asStateFlow()

    private val _animeFilter = MutableStateFlow(MalAnimeWatchingStatus.PlanToWatch)
    val animeFilter: StateFlow<MalAnimeWatchingStatus> = _animeFilter.asStateFlow()

    private val _mangaFilter = MutableStateFlow(MalMangaReadingStatus.PlanToRead)
    val mangaFilter: StateFlow<MalMangaReadingStatus> = _mangaFilter.asStateFlow()

    private val _listState = MutableStateFlow<ListState>(ListState.Loading)
    val listState: StateFlow<ListState> = _listState.asStateFlow()

    private var loadJob: Job? = null

    init {
        viewModelScope.launch {
            loadList()
        }
    }

    fun malLogin(openUri: (String) -> Unit) {
        openUri(malRepository.loginUri())
    }

    fun malLogout() {
        viewModelScope.launch {
            malRepository.logout()
        }
    }

    fun selectTab(tab: MalTab) {
        if (_activeTab.value == tab) return
        _activeTab.value = tab
        loadList()
    }

    fun selectAnimeFilter(status: MalAnimeWatchingStatus) {
        if (_animeFilter.value == status) return
        _animeFilter.value = status
        if (_activeTab.value == MalTab.Anime) loadList()
    }

    fun selectMangaFilter(status: MalMangaReadingStatus) {
        if (_mangaFilter.value == status) return
        _mangaFilter.value = status
        if (_activeTab.value == MalTab.Manga) loadList()
    }

    private fun loadList() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _listState.value = ListState.Loading

            val userState = malUserFlow.first()
            val username = (userState as? MalUserState.Authorized)?.user?.name

            when (_activeTab.value) {
                MalTab.Anime -> loadAnimeList(username)
                MalTab.Manga -> loadMangaList(username)
            }
        }
    }

    private suspend fun loadAnimeList(username: String?) {
        val status = _animeFilter.value.apiValue
        var offset = 0
        val animes = mutableListOf<MalAnime>()

        var response = malRepository.getUserAnimeList(
            username = username,
            offset = offset,
            status = status
        )
        if (response.isFailure) {
            _listState.value = ListState.Error(response.exceptionOrNull())
            return
        }

        while (response.isSuccess) {
            val body = response.getOrNull() ?: break
            if (body.data.isEmpty()) break
            animes.addAll(body.data.map { it.anime })
            offset += body.data.size
            if (body.data.size < 100) break
            response = malRepository.getUserAnimeList(
                username = username,
                offset = offset,
                status = status
            )
        }

        _listState.value = if (animes.isNotEmpty()) {
            ListState.AnimeContent(animes.sortedByDescending { it.mean })
        } else {
            ListState.Empty
        }
    }

    private suspend fun loadMangaList(username: String?) {
        val status = _mangaFilter.value.apiValue
        var offset = 0
        val mangas = mutableListOf<MalManga>()

        var response = malRepository.getUserMangaList(
            username = username,
            offset = offset,
            status = status
        )
        if (response.isFailure) {
            _listState.value = ListState.Error(response.exceptionOrNull())
            return
        }

        while (response.isSuccess) {
            val body = response.getOrNull() ?: break
            if (body.data.isEmpty()) break
            mangas.addAll(body.data.map { it.manga })
            offset += body.data.size
            if (body.data.size < 100) break
            response = malRepository.getUserMangaList(
                username = username,
                offset = offset,
                status = status
            )
        }

        _listState.value = if (mangas.isNotEmpty()) {
            ListState.MangaContent(mangas.sortedByDescending { it.mean })
        } else {
            ListState.Empty
        }
    }
}
