package com.example.coursework_app.domain.model.notes

class Note(
    val id: String,
    val emotion: String,
    val notes: Map<String, DayNote>,
)
