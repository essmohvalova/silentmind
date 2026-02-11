package com.example.coursework_app.di

import android.content.Context
import androidx.room.Room
import com.example.coursework_app.data.db.AppDatabase
import com.example.coursework_app.data.db.dao.UserDao
import com.example.coursework_app.data.mappers.users.UserMapper
import com.example.coursework_app.domain.repository.UserRepository
import com.example.coursework_app.data.repository.UserRepositoryImpl
import com.example.coursework_app.domain.usecase.GetUserUseCase
import com.example.coursework_app.domain.usecase.SaveUserUseCase
import com.example.coursework_app.domain.usecase.ObserveUserUseCase
import com.example.coursework_app.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {

        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            context.getString(R.string.db_name)
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    fun provideUserMapper(): UserMapper {
        return UserMapper()
    }

    @Provides
    fun provideUserRepository(
        userDao: UserDao,
        mapper: UserMapper
    ): UserRepository {
        return UserRepositoryImpl(userDao, mapper)
    }

    @Provides
    fun provideSaveUserUseCase(repository: UserRepository): SaveUserUseCase {
        return SaveUserUseCase(repository)
    }

    @Provides
    fun provideGetUserUseCase(repository: UserRepository): GetUserUseCase {
        return GetUserUseCase(repository)
    }

    @Provides
    fun provideObserveUserUseCase(repository: UserRepository): ObserveUserUseCase {
        return ObserveUserUseCase(repository)
    }
}