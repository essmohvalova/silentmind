package com.example.coursework_app.domain.model.notes

import java.time.LocalDate

data class Note(
    val id: String,
    val emotion: EmotionType,
    val notes: Map<LocalDate, DayNote>,
)
