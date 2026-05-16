package com.example.coursework_app.ui.journal

data class JournalEntryUi(
    val id: String,
    val emotion: String,
    val intensity: Int,
    val text: String?,
    val formattedCreatedAt: String,
)
