package com.grig.myanimelist.ui.seasons

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.grig.myanimelist.MalRoute
import com.grig.myanimelist.data.MalRepository
import com.grig.myanimelist.data.local.WatchlistDao
import com.grig.myanimelist.data.model.MalUserState
import com.grig.myanimelist.data.model.anime.MalAnimeListStatus
import com.grig.myanimelist.data.model.anime.MalAnimeWatchingStatus
import com.grig.myanimelist.ui.animelist.AnimeCardData
import com.grig.myanimelist.ui.home.SortDirection
import com.grig.myanimelist.ui.home.sortedWithOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeasonsViewModel @Inject constructor(
    private val malRepository: MalRepository,
    watchlistDao: WatchlistDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val guestUsername: String? =
        savedStateHandle.toRoute<MalRoute.Seasons>().username.trim().ifEmpty { null }

    private val _state = MutableStateFlow<SeasonsState>(SeasonsState.Loading)
    val state: StateFlow<SeasonsState> = _state.asStateFlow()

    private val _editSheetAnime = MutableStateFlow<AnimeCardData?>(null)
    val editSheetAnime: StateFlow<AnimeCardData?> = _editSheetAnime.asStateFlow()

    val authorized: StateFlow<Boolean> = malRepository.userFlow
        .map { it is MalUserState.Authorized }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val watchlistIds: StateFlow<Set<Int>> = watchlistDao.getAllIds()
        .map { it.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    private var cached: List<AnimeCardData> = emptyList()

    init {
        load()
    }

    fun retry() = load()

    private fun load() {
        viewModelScope.launch {
            _state.value = SeasonsState.Loading
            val username = when (malRepository.userFlow.first()) {
                is MalUserState.Authorized -> null // repository falls back to "@me"
                is MalUserState.Unauthorized -> guestUsername ?: run {
                    _state.value = SeasonsState.Empty
                    return@launch
                }
            }
            val result = malRepository.getAllUserAnime(
                username = username,
                status = MalAnimeWatchingStatus.PlanToWatch.apiValue
            )
            result.fold(
                onSuccess = { nodes ->
                    cached = nodes.map { AnimeCardData(it.anime, it.listStatus) }
                    publish()
                },
                onFailure = { _state.value = SeasonsState.Error(it) }
            )
        }
    }

    private fun publish() {
        if (cached.isEmpty()) {
            _state.value = SeasonsState.Empty
            return
        }
        _state.value = SeasonsState.Content(groupBySeason(cached))
    }

    fun onAnimeLongClick(data: AnimeCardData) {
        _editSheetAnime.value = data
    }

    fun dismissEditSheet() {
        _editSheetAnime.value = null
    }

    fun onAnimeUpdated(animeId: Int, newStatus: MalAnimeListStatus) {
        _editSheetAnime.value = null
        cached = if (newStatus.status == MalAnimeWatchingStatus.PlanToWatch) {
            cached.map { if (it.anime.id == animeId) it.copy(listStatus = newStatus) else it }
        } else {
            cached.filter { it.anime.id != animeId }
        }
        publish()
    }

    fun onAnimeDeleted(animeId: Int) {
        _editSheetAnime.value = null
        cached = cached.filter { it.anime.id != animeId }
        publish()
    }
}

/** Groups newest season first, "Date TBA" last; each group sorted by MAL score descending. */
fun groupBySeason(animes: List<AnimeCardData>): List<SeasonGroup> =
    animes
        .groupBy { it.anime.resolveSeason() }
        .entries
        .sortedWith(
            Comparator { a, b ->
                when {
                    a.key == null && b.key == null -> 0
                    a.key == null -> 1
                    b.key == null -> -1
                    else -> b.key!!.compareTo(a.key!!)
                }
            }
        )
        .map { (season, items) ->
            SeasonGroup(
                season = season,
                animes = sortedWithOption(items, SortDirection.Descending) { it.anime.mean }
            )
        }
