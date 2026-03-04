package com.example.coursework_app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.coursework_app.data.db.dao.UserDao
import com.example.coursework_app.data.db.entity.UserEntityDb

@Database(
    entities = [UserEntityDb::class],
    version = 1,
    exportSchema = false
)
abstract class UseDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        const val DATABASE_NAME = "emotion_tracker.db" //нужно ли выносить в константу?
    }
}