package com.example.coursework_app.ui.journal

import com.example.coursework_app.domain.model.mood.MoodEntry
import java.util.Calendar

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
