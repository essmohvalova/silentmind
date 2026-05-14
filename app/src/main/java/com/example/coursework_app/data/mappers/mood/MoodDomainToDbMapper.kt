package com.example.coursework_app.data.mappers.mood

import com.example.coursework_app.data.db.entity.mood.MoodEntryEntity
import com.example.coursework_app.domain.model.mood.MoodEntry
import javax.inject.Inject

class MoodDomainToDbMapper @Inject constructor() : (MoodEntry) -> MoodEntryEntity {

    override fun invoke(mood: MoodEntry): MoodEntryEntity {
        return MoodEntryEntity(
            id = mood.id,
            userId = mood.userId,
            emotion = mood.emotion,
            intensity = mood.intensity,
            text = mood.text,
            createdAt = mood.createdAt,
        )
    }
}