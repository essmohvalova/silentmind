package com.example.coursework_app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.coursework_app.data.db.dao.UserDao
import com.example.coursework_app.data.db.entity.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}