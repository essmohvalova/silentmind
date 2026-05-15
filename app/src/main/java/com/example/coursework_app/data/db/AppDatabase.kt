package com.example.coursework_app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.coursework_app.data.db.converters.NoteEntityConverter
import com.example.coursework_app.data.db.dao.MoodEntryDao
import com.example.coursework_app.data.db.dao.NotesDao
import com.example.coursework_app.data.db.dao.UserDao
import com.example.coursework_app.data.db.entity.notes.NoteEntityDb
import com.example.coursework_app.data.db.entity.user.UserEntityDb
import com.example.coursework_app.data.db.entity.mood.MoodEntryEntity
@Database(
    entities = [
        UserEntityDb::class,
        NoteEntityDb::class,
        MoodEntryEntity::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(NoteEntityConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun notesDao(): NotesDao
    abstract fun moodEntryDao() : MoodEntryDao
}
