package com.example.coursework_app.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.coursework_app.data.db.entity.user.UserEntityDb
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntityDb)

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUser(id: String): UserEntityDb?

    @Query("SELECT * FROM users WHERE id = :id")
    fun observeUser(id : String): Flow<UserEntityDb?>

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()
}
