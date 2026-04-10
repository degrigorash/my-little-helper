package com.grig.myanimelist.ui.animelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grig.myanimelist.data.MalRepository
import com.grig.myanimelist.data.model.MalUserState
import com.grig.myanimelist.data.model.anime.MalAnime
import com.grig.myanimelist.data.model.anime.MalAnimeAiringStatus
import com.grig.myanimelist.data.model.anime.MalAnimeListStatus
import com.grig.myanimelist.data.model.anime.MalAnimeWatchingStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeListViewModel @Inject constructor(
    private val malRepository: MalRepository
) : ViewModel() {

    private val _listState = MutableStateFlow<AnimeListState>(AnimeListState.Loading)
    val listState: StateFlow<AnimeListState> = _listState.asStateFlow()

    private val _statusFilter = MutableStateFlow<Set<MalAnimeWatchingStatus>>(emptySet())
    val statusFilter: StateFlow<Set<MalAnimeWatchingStatus>> = _statusFilter.asStateFlow()

    private val _upcomingFilter = MutableStateFlow(false)
    val upcomingFilter: StateFlow<Boolean> = _upcomingFilter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _editSheetAnime = MutableStateFlow<AnimeCardData?>(null)
    val editSheetAnime: StateFlow<AnimeCardData?> = _editSheetAnime.asStateFlow()

    private var loadJob: Job? = null
    private var cached: List<Pair<MalAnime, MalAnimeListStatus?>> = emptyList()
    private var guestUsername: String = ""

    init {
        viewModelScope.launch {
            val userState = malRepository.userFlow.first()
            if (userState is MalUserState.Authorized) {
                loadList()
            } else {
                _listState.value = AnimeListState.Empty
            }
        }
    }

    fun setGuestUsername(name: String) {
        guestUsername = name
    }

    fun searchGuestList() {
        loadList()
    }

    fun selectFilter(status: MalAnimeWatchingStatus) {
        val current = _statusFilter.value
        val updated = if (status in current) current - status else current + status
        _statusFilter.value = updated
        applyFilter()
    }

    fun toggleUpcomingFilter() {
        val updated = !_upcomingFilter.value
        _upcomingFilter.value = updated
        applyFilter()
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        applyFilter()
    }

    fun refresh() {
        _isRefreshing.value = true
        loadList()
    }

    fun retry() {
        loadList()
    }

    private fun loadList() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _listState.value = AnimeListState.Loading

            val username = resolveUsername()
            if (username == null) {
                _listState.value = AnimeListState.Empty
                _isRefreshing.value = false
                return@launch
            }

            loadAnimeList(username)
            _isRefreshing.value = false
        }
    }

    private suspend fun resolveUsername(): String? {
        val userState = malRepository.userFlow.first()
        return when (userState) {
            is MalUserState.Authorized -> userState.user.name
            is MalUserState.Unauthorized -> guestUsername.trim().ifEmpty { null }
        }
    }

    private suspend fun loadAnimeList(username: String?) {
        var offset = 0
        val animes = mutableListOf<Pair<MalAnime, MalAnimeListStatus?>>()

        var response = malRepository.getUserAnimeList(username = username, offset = offset)
        if (response.isFailure) {
            _listState.value = AnimeListState.Error(response.exceptionOrNull())
            return
        }

        while (response.isSuccess) {
            val body = response.getOrNull() ?: break
            if (body.data.isEmpty()) break
            animes.addAll(body.data.map { it.anime to it.listStatus })
            offset += body.data.size
            if (body.data.size < 100) break
            response = malRepository.getUserAnimeList(username = username, offset = offset)
        }

        cached = animes
        applyFilter()
    }

    private fun applyFilter() {
        val filter = _statusFilter.value
        var filtered = if (filter.isEmpty()) cached else cached.filter { it.second?.status in filter }
        if (_upcomingFilter.value) {
            filtered = filtered.filter {
                it.first.status == MalAnimeAiringStatus.NotYetAired ||
                    it.first.status == MalAnimeAiringStatus.CurrentlyAiring
            }
        }
        val query = _searchQuery.value
        if (query.isNotBlank()) {
            filtered = filtered.filter { (anime, _) ->
                anime.title.contains(query, ignoreCase = true) ||
                    anime.alternativeTitles.en?.contains(query, ignoreCase = true) == true ||
                    anime.alternativeTitles.ja?.contains(query, ignoreCase = true) == true ||
                    anime.alternativeTitles.synonyms.any { it.contains(query, ignoreCase = true) }
            }
        }

        val cardData = filtered
            .map { AnimeCardData(it.first, it.second) }
            .sortedByDescending { it.anime.mean }

        _listState.value = if (cardData.isNotEmpty()) {
            AnimeListState.Content(cardData)
        } else if (cached.isNotEmpty()) {
            AnimeListState.Content(emptyList())
        } else {
            AnimeListState.Empty
        }
    }

    fun onAnimeClick(data: AnimeCardData) {
        _editSheetAnime.value = data
    }

    fun dismissEditSheet() {
        _editSheetAnime.value = null
    }

    fun onAnimeUpdated(animeId: Int, newStatus: MalAnimeListStatus) {
        cached = cached.map { (anime, status) ->
            if (anime.id == animeId) anime to newStatus else anime to status
        }
        applyFilter()
        _editSheetAnime.value = null
    }

    fun onAnimeDeleted(animeId: Int) {
        cached = cached.filter { it.first.id != animeId }
        applyFilter()
        _editSheetAnime.value = null
    }
}
