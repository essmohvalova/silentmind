package com.example.coursework_app.di

import com.example.coursework_app.domain.preferences.UserPreferences
import com.example.coursework_app.domain.repository.UserRepository
import com.example.coursework_app.domain.usecase.GetUserUseCase
import com.example.coursework_app.domain.usecase.GetUserUseCaseImpl
import com.example.coursework_app.domain.usecase.IsUserAuthorizedUseCase
import com.example.coursework_app.domain.usecase.IsUserAuthorizedUseCaseImpl
import com.example.coursework_app.domain.usecase.ObserveUserUseCase
import com.example.coursework_app.domain.usecase.ObserveUserUseCaseImpl
import com.example.coursework_app.domain.usecase.SaveUserUseCase
import com.example.coursework_app.domain.usecase.SaveUserUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {

    @Provides
    fun provideSaveUserUseCase(repository: UserRepository): SaveUserUseCase {
        return SaveUserUseCaseImpl(repository)
    }

    @Provides
    fun provideGetUserUseCase(repository: UserRepository): GetUserUseCase {
        return GetUserUseCaseImpl(repository)
    }

    @Provides
    fun provideObserveUserUseCase(repository: UserRepository): ObserveUserUseCase {
        return ObserveUserUseCaseImpl(repository)
    }

    @Provides
    fun provideIsUserAuthorizedUseCase(userPreferences: UserPreferences): IsUserAuthorizedUseCase {
        return IsUserAuthorizedUseCaseImpl(userPreferences)
    }
}
