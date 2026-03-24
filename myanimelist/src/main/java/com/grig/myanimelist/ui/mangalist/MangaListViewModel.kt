package com.grig.myanimelist.ui.mangalist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grig.myanimelist.data.FilterPreferencesManager
import com.grig.myanimelist.data.MalRepository
import com.grig.myanimelist.data.model.MalUserState
import com.grig.myanimelist.data.model.manga.MalManga
import com.grig.myanimelist.data.model.manga.MalMangaListStatus
import com.grig.myanimelist.data.model.manga.MalMangaPublishStatus
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
class MangaListViewModel @Inject constructor(
    private val malRepository: MalRepository,
    private val filterPreferences: FilterPreferencesManager
) : ViewModel() {

    private val _listState = MutableStateFlow<MangaListState>(MangaListState.Loading)
    val listState: StateFlow<MangaListState> = _listState.asStateFlow()

    private val _statusFilter = MutableStateFlow<Set<MalMangaReadingStatus>>(emptySet())
    val statusFilter: StateFlow<Set<MalMangaReadingStatus>> = _statusFilter.asStateFlow()

    private val _upcomingFilter = MutableStateFlow(false)
    val upcomingFilter: StateFlow<Boolean> = _upcomingFilter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _editSheetManga = MutableStateFlow<MangaCardData?>(null)
    val editSheetManga: StateFlow<MangaCardData?> = _editSheetManga.asStateFlow()

    private var loadJob: Job? = null
    private var cached: List<Pair<MalManga, MalMangaListStatus?>> = emptyList()
    private var guestUsername: String = ""

    init {
        viewModelScope.launch {
            _statusFilter.value = filterPreferences.loadMangaFilter()
            _upcomingFilter.value = filterPreferences.loadUpcomingFilter()

            val userState = malRepository.userFlow.first()
            if (userState is MalUserState.Authorized) {
                loadList()
            } else {
                _listState.value = MangaListState.Empty
            }
        }
    }

    fun setGuestUsername(name: String) {
        guestUsername = name
    }

    fun searchGuestList() {
        loadList()
    }

    fun selectFilter(status: MalMangaReadingStatus) {
        val current = _statusFilter.value
        val updated = if (status in current) current - status else current + status
        _statusFilter.value = updated
        viewModelScope.launch { filterPreferences.saveMangaFilter(updated) }
        applyFilter()
    }

    fun toggleUpcomingFilter() {
        val updated = !_upcomingFilter.value
        _upcomingFilter.value = updated
        viewModelScope.launch { filterPreferences.saveUpcomingFilter(updated) }
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
            _listState.value = MangaListState.Loading

            val username = resolveUsername()
            if (username == null) {
                _listState.value = MangaListState.Empty
                _isRefreshing.value = false
                return@launch
            }

            loadMangaList(username)
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

    private suspend fun loadMangaList(username: String?) {
        var offset = 0
        val mangas = mutableListOf<Pair<MalManga, MalMangaListStatus?>>()

        var response = malRepository.getUserMangaList(username = username, offset = offset)
        if (response.isFailure) {
            _listState.value = MangaListState.Error(response.exceptionOrNull())
            return
        }

        while (response.isSuccess) {
            val body = response.getOrNull() ?: break
            if (body.data.isEmpty()) break
            mangas.addAll(body.data.map { it.manga to it.listStatus })
            offset += body.data.size
            if (body.data.size < 100) break
            response = malRepository.getUserMangaList(username = username, offset = offset)
        }

        cached = mangas
        applyFilter()
    }

    private fun applyFilter() {
        val filter = _statusFilter.value
        var filtered = if (filter.isEmpty()) cached else cached.filter { it.second?.status in filter }
        if (_upcomingFilter.value) {
            filtered = filtered.filter {
                it.first.status == MalMangaPublishStatus.NotYetPublished ||
                    it.first.status == MalMangaPublishStatus.CurrentlyPublishing
            }
        }
        val query = _searchQuery.value
        if (query.isNotBlank()) {
            filtered = filtered.filter { (manga, _) ->
                manga.title.contains(query, ignoreCase = true) ||
                    manga.alternativeTitles.en?.contains(query, ignoreCase = true) == true ||
                    manga.alternativeTitles.ja?.contains(query, ignoreCase = true) == true ||
                    manga.alternativeTitles.synonyms.any { it.contains(query, ignoreCase = true) }
            }
        }

        val cardData = filtered
            .map { MangaCardData(it.first, it.second) }
            .sortedByDescending { it.manga.mean }

        _listState.value = if (cardData.isNotEmpty()) {
            MangaListState.Content(cardData)
        } else if (cached.isNotEmpty()) {
            MangaListState.Content(emptyList())
        } else {
            MangaListState.Empty
        }
    }

    fun onMangaClick(data: MangaCardData) {
        _editSheetManga.value = data
    }

    fun dismissEditSheet() {
        _editSheetManga.value = null
    }

    fun onMangaUpdated(mangaId: Int, newStatus: MalMangaListStatus) {
        cached = cached.map { (manga, status) ->
            if (manga.id == mangaId) manga to newStatus else manga to status
        }
        applyFilter()
        _editSheetManga.value = null
    }

    fun onMangaDeleted(mangaId: Int) {
        cached = cached.filter { it.first.id != mangaId }
        applyFilter()
        _editSheetManga.value = null
    }
}
