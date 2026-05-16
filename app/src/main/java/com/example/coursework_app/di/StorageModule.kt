package com.example.coursework_app.di

import android.content.Context
import androidx.room.Room
import com.example.coursework_app.R
import com.example.coursework_app.data.db.AppDatabase
import com.example.coursework_app.data.db.AppDatabaseMigrations.MIGRATION_2_3
import com.example.coursework_app.data.db.converters.NoteEntityConverter
import com.example.coursework_app.data.db.dao.MoodEntryDao
import com.example.coursework_app.data.db.dao.NotesDao
import com.example.coursework_app.data.db.dao.UserDao
import com.example.coursework_app.data.preferences.UserPreferencesImpl
import com.example.coursework_app.domain.preferences.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        noteConverters: NoteEntityConverter,
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            context.getString(R.string.db_name)
        )
            .addMigrations(MIGRATION_2_3)
            .fallbackToDestructiveMigration()
            .addTypeConverter(noteConverters)
            .build()
    }

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    fun provideNotesDao(database: AppDatabase): NotesDao {
        return database.notesDao()
    }

    @Provides
    @Singleton
    fun provideUserPreferences(
        @ApplicationContext context: Context
    ): UserPreferences {
        return UserPreferencesImpl(context)
    }

    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
        }
    }

    @Provides
    @Singleton
    fun provideNoteConverters(json: Json): NoteEntityConverter {
        return NoteEntityConverter(json)
    }

    @Provides
    fun provideMoodEntryDao(database: AppDatabase): MoodEntryDao {
        return database.moodEntryDao()
    }
}
