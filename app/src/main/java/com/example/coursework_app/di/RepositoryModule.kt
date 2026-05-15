package com.example.coursework_app.di

import com.example.coursework_app.data.db.dao.UserDao
import com.example.coursework_app.data.mappers.users.UserDbToDomainMapper
import com.example.coursework_app.data.mappers.users.UserDomainToDbMapper
import com.example.coursework_app.data.repository.UserRepositoryImpl
import com.example.coursework_app.domain.preferences.UserPreferences
import com.example.coursework_app.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.example.coursework_app.data.db.dao.MoodEntryDao
import com.example.coursework_app.data.mappers.mood.MoodEntryMapper
import com.example.coursework_app.data.repository.MoodEntryRepositoryImpl
import com.example.coursework_app.domain.repository.MoodEntryRepository

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        userDao: UserDao,
        dbToDomainMapper: UserDbToDomainMapper,
        domainToDbMapper: UserDomainToDbMapper,
        userPreferences: UserPreferences
    ): UserRepository {
        return UserRepositoryImpl(
            userDao = userDao,
            dbToDomainMapper = dbToDomainMapper,
            domainToDbMapper = domainToDbMapper,
            userPreferences = userPreferences,
        )
    }

    @Provides
    @Singleton
    fun provideMoodEntryRepository(
        dao: MoodEntryDao,
        mapper: MoodEntryMapper,
    ): MoodEntryRepository {
        return MoodEntryRepositoryImpl(
            dao = dao,
            mapper = mapper,
        )
    }
}
