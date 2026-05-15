package com.example.coursework_app.data.repository

import com.example.coursework_app.data.db.dao.MoodEntryDao
import com.example.coursework_app.data.mappers.mood.MoodEntryMapper
import com.example.coursework_app.domain.model.mood.MoodEntry
import com.example.coursework_app.domain.repository.MoodEntryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MoodEntryRepositoryImpl @Inject constructor(
    private val dao: MoodEntryDao,
    private val mapper: MoodEntryMapper,
) : MoodEntryRepository {

    override suspend fun saveMoodEntry(entry: MoodEntry) {
        dao.insert(mapper.toEntity(entry))
    }

    override fun observeMoodEntries(userId: String): Flow<List<MoodEntry>> {
        return dao.observeByUserId(userId)
            .map { entries -> entries.map(mapper::toDomain) }
    }
}