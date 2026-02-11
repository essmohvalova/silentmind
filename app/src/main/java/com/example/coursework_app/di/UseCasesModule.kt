package com.example.coursework_app.di

import com.example.coursework_app.domain.repository.UserRepository
import com.example.coursework_app.domain.usecase.GetUserUseCase
import com.example.coursework_app.domain.usecase.ObserveUserUseCase
import com.example.coursework_app.domain.usecase.SaveUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {

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