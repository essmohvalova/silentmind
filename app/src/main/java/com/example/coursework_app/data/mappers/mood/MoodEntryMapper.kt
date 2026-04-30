package com.example.coursework_app.data.mappers.mood

import com.example.coursework_app.data.db.entity.mood.MoodEntryEntity
import com.example.coursework_app.domain.model.mood.MoodEntry
import javax.inject.Inject

class MoodEntryMapper @Inject constructor() {

    fun toEntity(model: MoodEntry): MoodEntryEntity {
        return MoodEntryEntity(
            id = model.id,
            userId = model.userId,
            emotion = model.emotion,
            intensity = model.intensity,
            text = model.text,
            createdAt = model.createdAt,
        )
    }

    fun toDomain(entity: MoodEntryEntity): MoodEntry {
        return MoodEntry(
            id = entity.id,
            userId = entity.userId,
            emotion = entity.emotion,
            intensity = entity.intensity,
            text = entity.text,
            createdAt = entity.createdAt,
        )
    }
}