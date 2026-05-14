package com.example.coursework_app.ui.journal

data class JournalUiState(
    val entriesCount: Int = 0,
    val entries: List<JournalEntryUi> = emptyList(),
    val isLoading: Boolean = true,
)
