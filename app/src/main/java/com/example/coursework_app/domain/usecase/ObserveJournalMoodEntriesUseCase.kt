package com.example.coursework_app.domain.usecase

import com.example.coursework_app.domain.model.mood.MoodEntry
import com.example.coursework_app.domain.preferences.UserPreferences
import com.example.coursework_app.domain.repository.MoodEntryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface ObserveJournalMoodEntriesUseCase {

    operator fun invoke(): Flow<List<MoodEntry>>
}

class ObserveJournalMoodEntriesUseCaseImpl @Inject constructor(
    private val moodEntryRepository: MoodEntryRepository,
    private val userPreferences: UserPreferences,
) : ObserveJournalMoodEntriesUseCase {

    override fun invoke(): Flow<List<MoodEntry>> {
        val userId = userPreferences.getUserId() ?: return flowOf(emptyList())
        return moodEntryRepository.observeMoodEntries(userId)
            .map { entries -> entries.sortedByDescending { it.createdAt } }
    }
}
