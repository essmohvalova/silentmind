package com.example.coursework_app.domain.model.mood

class MoodEntry(
    val id: String,
    val userId: String,
    val emotion: String,
    val intensity: Int,
    val text: String?,
    val createdAt: Long,
)