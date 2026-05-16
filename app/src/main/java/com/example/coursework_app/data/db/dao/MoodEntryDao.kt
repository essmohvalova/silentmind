package com.example.coursework_app.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.coursework_app.data.db.entity.mood.MoodEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodEntryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: MoodEntryEntity)

    @Query("SELECT * FROM mood_entries WHERE userId = :userId ORDER BY createdAt DESC")
    fun observeByUserId(userId: String): Flow<List<MoodEntryEntity>>

    @Query("SELECT * FROM mood_entries WHERE userId = :userId AND createdAt BETWEEN :from AND :to ORDER BY createdAt DESC")
    fun observeByPeriod(
        userId: String,
        from: Long,
        to: Long,
    ): Flow<List<MoodEntryEntity>>
}