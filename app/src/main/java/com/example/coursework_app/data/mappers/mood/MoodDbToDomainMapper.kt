package com.example.coursework_app.data.mappers.mood

import com.example.coursework_app.data.db.entity.mood.MoodEntryEntity
import com.example.coursework_app.domain.model.mood.MoodEntry
import javax.inject.Inject

class MoodDbToDomainMapper @Inject constructor() : (MoodEntryEntity) -> MoodEntry {

    override fun invoke(moodEntity: MoodEntryEntity): MoodEntry {
        return MoodEntry(
            id = moodEntity.id,
            userId = moodEntity.userId,
            emotion = moodEntity.emotion,
            intensity = moodEntity.intensity,
            text = moodEntity.text,
            createdAt = moodEntity.createdAt,
        )
    }
}