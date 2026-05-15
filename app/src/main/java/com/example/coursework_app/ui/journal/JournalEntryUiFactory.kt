package com.example.coursework_app.ui.journal

import com.example.coursework_app.domain.model.mood.MoodEntry
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class JournalEntryUiFactory @Inject constructor() {

    fun fromDomain(entry: MoodEntry): JournalEntryUi {
        val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        return JournalEntryUi(
            id = entry.id,
            emotion = entry.emotion,
            intensity = entry.intensity,
            text = entry.text,
            formattedCreatedAt = formatter.format(Date(entry.createdAt)),
        )
    }
}
