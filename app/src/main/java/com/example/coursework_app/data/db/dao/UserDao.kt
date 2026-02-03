// data/db/dao/UserDao.kt
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

    companion object {
        const val TABLE_NAME = "users"
        const val USER_ID = 1
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("SELECT * FROM $TABLE_NAME WHERE id = $USER_ID")
    suspend fun getUser(): UserEntity?

    @Query("SELECT * FROM $TABLE_NAME WHERE id = $USER_ID")
    fun observeUser(): Flow<UserEntity?>

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun deleteAllUsers()
}