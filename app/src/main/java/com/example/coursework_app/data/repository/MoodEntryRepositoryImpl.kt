package com.example.coursework_app.data.repository

import com.example.coursework_app.data.db.dao.MoodEntryDao
import com.example.coursework_app.data.mappers.mood.MoodDbToDomainMapper
import com.example.coursework_app.data.mappers.mood.MoodDomainToDbMapper
import com.example.coursework_app.domain.model.mood.MoodEntry
import com.example.coursework_app.domain.repository.MoodEntryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MoodEntryRepositoryImpl @Inject constructor(
    private val dao: MoodEntryDao,
    private val moodDbToDomainMapper: MoodDbToDomainMapper,
    private val moodDomainToDbMapper: MoodDomainToDbMapper
) : MoodEntryRepository {

    override suspend fun saveMoodEntry(entry: MoodEntry) {
        dao.insert(moodDomainToDbMapper(entry))
    }

    override fun observeMoodEntries(userId: String): Flow<List<MoodEntry>> {
        return dao.observeByUserId(userId)
            .map { entries -> entries.map {
                entity -> moodDbToDomainMapper(entity)
            } }
    }
}