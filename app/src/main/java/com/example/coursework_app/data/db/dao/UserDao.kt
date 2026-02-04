package com.example.coursework_app.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.coursework_app.data.db.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE id = 1")
    suspend fun getUser(): UserEntity?

    @Query("SELECT * FROM users WHERE id = 1")
    fun observeUser(): Flow<UserEntity?>

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()
}