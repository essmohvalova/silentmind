package com.example.coursework_app.domain.repository

import com.example.coursework_app.domain.model.mood.MoodEntry
import kotlinx.coroutines.flow.Flow

interface MoodEntryRepository {

    suspend fun saveMoodEntry(entry: MoodEntry)

    fun observeMoodEntries(userId: String): Flow<List<MoodEntry>>
}