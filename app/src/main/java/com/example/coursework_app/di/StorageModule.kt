package com.example.coursework_app.di

import android.content.Context
import androidx.room.Room
import com.example.coursework_app.R
import com.example.coursework_app.data.db.UseDatabase
import com.example.coursework_app.data.db.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): UseDatabase {
        return Room.databaseBuilder(
            context,
            UseDatabase::class.java,
            context.getString(R.string.db_name)
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideUserDao(database: UseDatabase): UserDao {
        return database.userDao()
    }
}