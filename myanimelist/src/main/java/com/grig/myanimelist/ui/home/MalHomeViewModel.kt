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

    private val _animeFilter = MutableStateFlow<Set<MalAnimeWatchingStatus>>(emptySet())
    val animeFilter: StateFlow<Set<MalAnimeWatchingStatus>> = _animeFilter.asStateFlow()

    private val _mangaFilter = MutableStateFlow<Set<MalMangaReadingStatus>>(emptySet())
    val mangaFilter: StateFlow<Set<MalMangaReadingStatus>> = _mangaFilter.asStateFlow()

    private val _listState = MutableStateFlow<ListState>(ListState.Loading)
    val listState: StateFlow<ListState> = _listState.asStateFlow()

    private val _guestUsername = MutableStateFlow("")
    val guestUsername: StateFlow<String> = _guestUsername.asStateFlow()

    private var loadJob: Job? = null

    private var cachedAnimes: List<Pair<MalAnime, MalAnimeWatchingStatus?>> = emptyList()
    private var cachedMangas: List<Pair<MalManga, MalMangaReadingStatus?>> = emptyList()

    init {
        viewModelScope.launch {
            val userState = malUserFlow.first()
            if (userState is MalUserState.Authorized) {
                loadList()
            } else {
                _listState.value = ListState.Empty
            }
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
        val current = _animeFilter.value
        _animeFilter.value = if (status in current) current - status else current + status
        if (_activeTab.value == MalTab.Anime) applyAnimeFilter()
    }

    fun selectMangaFilter(status: MalMangaReadingStatus) {
        val current = _mangaFilter.value
        _mangaFilter.value = if (status in current) current - status else current + status
        if (_activeTab.value == MalTab.Manga) applyMangaFilter()
    }

    fun setGuestUsername(name: String) {
        _guestUsername.value = name
    }

    fun searchGuestList() {
        loadList()
    }

    fun retry() {
        loadList()
    }

    private fun loadList() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _listState.value = ListState.Loading

            val userState = malUserFlow.first()
            val username = when (userState) {
                is MalUserState.Authorized -> userState.user.name
                is MalUserState.Unauthorized -> {
                    val guest = _guestUsername.value.trim()
                    if (guest.isEmpty()) {
                        _listState.value = ListState.Empty
                        return@launch
                    }
                    guest
                }
            }

            when (_activeTab.value) {
                MalTab.Anime -> loadAnimeList(username)
                MalTab.Manga -> loadMangaList(username)
            }
        }
    }

    private suspend fun loadAnimeList(username: String?) {
        var offset = 0
        val animes = mutableListOf<Pair<MalAnime, MalAnimeWatchingStatus?>>()

        var response = malRepository.getUserAnimeList(
            username = username,
            offset = offset
        )
        if (response.isFailure) {
            _listState.value = ListState.Error(response.exceptionOrNull())
            return
        }

        while (response.isSuccess) {
            val body = response.getOrNull() ?: break
            if (body.data.isEmpty()) break
            animes.addAll(body.data.map { it.anime to it.listStatus?.status })
            offset += body.data.size
            if (body.data.size < 100) break
            response = malRepository.getUserAnimeList(
                username = username,
                offset = offset
            )
        }

        cachedAnimes = animes
        applyAnimeFilter()
    }

    private suspend fun loadMangaList(username: String?) {
        var offset = 0
        val mangas = mutableListOf<Pair<MalManga, MalMangaReadingStatus?>>()

        var response = malRepository.getUserMangaList(
            username = username,
            offset = offset
        )
        if (response.isFailure) {
            _listState.value = ListState.Error(response.exceptionOrNull())
            return
        }

        while (response.isSuccess) {
            val body = response.getOrNull() ?: break
            if (body.data.isEmpty()) break
            mangas.addAll(body.data.map { it.manga to it.listStatus?.status })
            offset += body.data.size
            if (body.data.size < 100) break
            response = malRepository.getUserMangaList(
                username = username,
                offset = offset
            )
        }

        cachedMangas = mangas
        applyMangaFilter()
    }

    private fun applyAnimeFilter() {
        val filter = _animeFilter.value
        val filtered = if (filter.isEmpty()) {
            cachedAnimes.map { it.first }
        } else {
            cachedAnimes.filter { it.second in filter }.map { it.first }
        }

        _listState.value = if (filtered.isNotEmpty()) {
            ListState.AnimeContent(filtered.sortedByDescending { it.mean })
        } else {
            ListState.Empty
        }
    }

    private fun applyMangaFilter() {
        val filter = _mangaFilter.value
        val filtered = if (filter.isEmpty()) {
            cachedMangas.map { it.first }
        } else {
            cachedMangas.filter { it.second in filter }.map { it.first }
        }

        _listState.value = if (filtered.isNotEmpty()) {
            ListState.MangaContent(filtered.sortedByDescending { it.mean })
        } else {
            ListState.Empty
        }
    }
}
