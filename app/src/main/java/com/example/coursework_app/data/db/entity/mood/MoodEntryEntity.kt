package com.example.coursework_app.data.db.entity.mood

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mood_entries")
data class MoodEntryEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val emotion: String,
    val intensity: Int,
    val text: String?,
    val createdAt: Long,
)