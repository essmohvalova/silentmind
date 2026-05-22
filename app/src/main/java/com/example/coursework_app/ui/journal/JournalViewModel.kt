package com.example.coursework_app.ui.journal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework_app.domain.usecase.ObserveJournalMoodEntriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class JournalViewModel @Inject constructor(
    private val observeJournalMoodEntriesUseCase: ObserveJournalMoodEntriesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(JournalUiState())
    val uiState: StateFlow<JournalUiState> = _uiState
    private var observeEntriesJob: Job? = null

    init {
        loadEntries()
    }

    fun loadEntries() {
        observeEntriesJob?.cancel()
        _uiState.update { it.copy(isLoading = true) }

        observeEntriesJob = viewModelScope.launch {
            observeJournalMoodEntriesUseCase().collect { entries ->
                _uiState.update { state ->
                    state.copy(entries = entries, isLoading = false).withFilteredEntries()
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
