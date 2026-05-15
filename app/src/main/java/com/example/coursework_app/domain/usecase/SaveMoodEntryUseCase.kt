package com.example.coursework_app.domain.usecase

import com.example.coursework_app.domain.model.mood.MoodEntry
import com.example.coursework_app.domain.preferences.UserPreferences
import com.example.coursework_app.domain.repository.MoodEntryRepository
import java.util.UUID
import javax.inject.Inject

sealed interface SaveMoodEntryResult {
    data object Success : SaveMoodEntryResult
    data object NoLoggedInUser : SaveMoodEntryResult
    data object NoEmotionSelected : SaveMoodEntryResult
    data class Failure(val message: String) : SaveMoodEntryResult
}

interface SaveMoodEntryUseCase {

    suspend operator fun invoke(
        emotionName: String?,
        intensity: Int,
        noteText: String,
    ): SaveMoodEntryResult
}

class SaveMoodEntryUseCaseImpl @Inject constructor(
    private val moodEntryRepository: MoodEntryRepository,
    private val userPreferences: UserPreferences,
) : SaveMoodEntryUseCase {

    override suspend operator fun invoke(
        emotionName: String?,
        intensity: Int,
        noteText: String,
    ): SaveMoodEntryResult {
        if (emotionName.isNullOrBlank()) return SaveMoodEntryResult.NoEmotionSelected
        val userId = userPreferences.getUserId() ?: return SaveMoodEntryResult.NoLoggedInUser

        return runCatching {
            moodEntryRepository.saveMoodEntry(
                MoodEntry(
                    id = UUID.randomUUID().toString(),
                    userId = userId,
                    emotion = emotionName,
                    intensity = intensity,
                    text = noteText.takeIf { it.isNotBlank() },
                    createdAt = System.currentTimeMillis(),
                )
            )
        }.fold(
            onSuccess = { SaveMoodEntryResult.Success },
            onFailure = { SaveMoodEntryResult.Failure(it.message ?: "Unknown save error") },
        )
    }
}
