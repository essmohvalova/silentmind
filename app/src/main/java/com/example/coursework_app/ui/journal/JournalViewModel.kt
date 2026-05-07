package com.example.coursework_app.ui.journal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework_app.domain.model.mood.MoodEntry
import com.example.coursework_app.domain.repository.MoodEntryRepository
import com.example.coursework_app.domain.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class JournalViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val moodEntryRepository: MoodEntryRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(JournalUiState())
    val uiState: StateFlow<JournalUiState> = _uiState
    private var observeEntriesJob: Job? = null

    init {
        loadEntries()
    }

    fun loadEntries() {
        observeEntriesJob?.cancel()
        observeEntriesJob = null

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val user = getUserUseCase()
            val userId = user?.id

            if (userId == null) {
                _uiState.update {
                    it.copy(
                        entries = emptyList(),
                        filteredEntries = emptyList(),
                        isLoading = false
                    )
                }
                return@launch
            }

            observeEntriesJob = launch {
                moodEntryRepository.observeMoodEntries(userId).collect { entries ->
                    _uiState.update {
                        val updated = it.copy(entries = entries, isLoading = false)
                        updated.withFilteredEntries()
                    }
                }
            }
        }
    }

    fun selectEmotionFilter(emotion: String?) {
        _uiState.update { state ->
            state.copy(selectedEmotion = emotion).withFilteredEntries()
        }
    }

    fun selectDate(dateKey: JournalDateKey?) {
        _uiState.update { state ->
            val nextDate = if (state.selectedDate == dateKey) null else dateKey
            state.copy(selectedDate = nextDate).withFilteredEntries()
        }
    }

    fun selectDateFromMillis(dateMillis: Long) {
        selectDate(dateKeyFromTimestamp(dateMillis))
    }

    fun showAllPeriod() {
        _uiState.update { state ->
            state.copy(selectedDate = null).withFilteredEntries()
        }
    }

    fun shiftWeek(offset: Int) {
        _uiState.update {
            it.copy(
                weekStartMillis = shiftWeekMillis(
                    currentWeekStart = it.weekStartMillis,
                    offset = offset,
                ),
            )
        }
    }

    fun setSortMode(mode: JournalSortMode) {
        _uiState.update { state ->
            state.copy(sortMode = mode).withFilteredEntries()
        }
    }

    fun clearFilters() {
        _uiState.update { state ->
            state.copy(
                selectedEmotion = null,
                selectedDate = null,
            ).withFilteredEntries()
        }
    }

    private fun JournalUiState.withFilteredEntries(): JournalUiState {
        var items = entries
        selectedEmotion?.let { emotion ->
            items = items.filter { it.emotion == emotion }
        }
        selectedDate?.let { date ->
            items = items.filter { entry ->
                dateKeyFromTimestamp(entry.createdAt) == date
            }
        }
        items = when (sortMode) {
            JournalSortMode.NEWEST_FIRST -> items.sortedByDescending { it.createdAt }
            JournalSortMode.OLDEST_FIRST -> items.sortedBy { it.createdAt }
            JournalSortMode.INTENSITY_HIGH_FIRST -> items.sortedByDescending { it.intensity }
            JournalSortMode.INTENSITY_LOW_FIRST -> items.sortedBy { it.intensity }
        }
        return copy(
            filteredEntries = items,
            entriesCount = items.size,
        )
    }

    private fun currentWeekStartMillis(): Long {
        val calendar = Calendar.getInstance()
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DAY_OF_MONTH, -1)
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    private fun shiftWeekMillis(currentWeekStart: Long, offset: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = currentWeekStart
        calendar.add(Calendar.DAY_OF_MONTH, offset * 7)
        return calendar.timeInMillis
    }

    private fun dateKeyFromTimestamp(timestamp: Long): JournalDateKey {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        return JournalDateKey(
            year = calendar.get(Calendar.YEAR),
            month = calendar.get(Calendar.MONTH),
            day = calendar.get(Calendar.DAY_OF_MONTH),
        )
    }
}

data class JournalUiState(
    val entries: List<MoodEntry> = emptyList(),
    val filteredEntries: List<MoodEntry> = emptyList(),
    val entriesCount: Int = 0,
    val isLoading: Boolean = true,
    val selectedEmotion: String? = null,
    val selectedDate: JournalDateKey? = null,
    val sortMode: JournalSortMode = JournalSortMode.NEWEST_FIRST,
    val weekStartMillis: Long = Calendar.getInstance().run {
        while (get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            add(Calendar.DAY_OF_MONTH, -1)
        }
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
        timeInMillis
    },
)

enum class JournalSortMode {
    NEWEST_FIRST,
    OLDEST_FIRST,
    INTENSITY_HIGH_FIRST,
    INTENSITY_LOW_FIRST,
}

data class JournalDateKey(
    val year: Int,
    val month: Int,
    val day: Int,
)