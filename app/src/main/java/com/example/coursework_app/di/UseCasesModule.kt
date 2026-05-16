package com.example.coursework_app.di

import com.example.coursework_app.domain.preferences.UserPreferences
import com.example.coursework_app.domain.repository.AnalyticsRepository
import com.example.coursework_app.domain.repository.MoodEntryRepository
import com.example.coursework_app.domain.repository.UserRepository
import com.example.coursework_app.domain.usecase.CompleteOnboardingUseCase
import com.example.coursework_app.domain.usecase.CompleteOnboardingUseCaseImpl
import com.example.coursework_app.domain.usecase.GetAnalyticsUserIdUseCase
import com.example.coursework_app.domain.usecase.GetAnalyticsUserIdUseCaseImpl
import com.example.coursework_app.domain.usecase.GetUserUseCase
import com.example.coursework_app.domain.usecase.GetUserUseCaseImpl
import com.example.coursework_app.domain.usecase.IsUserAuthorizedUseCase
import com.example.coursework_app.domain.usecase.IsUserAuthorizedUseCaseImpl
import com.example.coursework_app.domain.usecase.LoadMainUserNameUseCase
import com.example.coursework_app.domain.usecase.LoadMainUserNameUseCaseImpl
import com.example.coursework_app.domain.usecase.ObserveAnalyticsSummaryUseCase
import com.example.coursework_app.domain.usecase.ObserveAnalyticsSummaryUseCaseImpl
import com.example.coursework_app.domain.usecase.ObserveJournalMoodEntriesUseCase
import com.example.coursework_app.domain.usecase.ObserveJournalMoodEntriesUseCaseImpl
import com.example.coursework_app.domain.usecase.ObserveUserUseCase
import com.example.coursework_app.domain.usecase.ObserveUserUseCaseImpl
import com.example.coursework_app.domain.usecase.SaveMoodEntryUseCase
import com.example.coursework_app.domain.usecase.SaveMoodEntryUseCaseImpl
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

    @Provides
    fun provideSaveMoodEntryUseCase(
        moodEntryRepository: MoodEntryRepository,
        userPreferences: UserPreferences,
    ): SaveMoodEntryUseCase {
        return SaveMoodEntryUseCaseImpl(moodEntryRepository, userPreferences)
    }

    @Provides
    fun provideObserveJournalMoodEntriesUseCase(
        moodEntryRepository: MoodEntryRepository,
        userPreferences: UserPreferences,
    ): ObserveJournalMoodEntriesUseCase {
        return ObserveJournalMoodEntriesUseCaseImpl(moodEntryRepository, userPreferences)
    }

    @Provides
    fun provideCompleteOnboardingUseCase(
        saveUserUseCase: SaveUserUseCase,
        userPreferences: UserPreferences,
    ): CompleteOnboardingUseCase {
        return CompleteOnboardingUseCaseImpl(saveUserUseCase, userPreferences)
    }

    @Provides
    fun provideGetAnalyticsUserIdUseCase(getUserUseCase: GetUserUseCase): GetAnalyticsUserIdUseCase {
        return GetAnalyticsUserIdUseCaseImpl(getUserUseCase)
    }

    @Provides
    fun provideObserveAnalyticsSummaryUseCase(
        analyticsRepository: AnalyticsRepository,
    ): ObserveAnalyticsSummaryUseCase {
        return ObserveAnalyticsSummaryUseCaseImpl(analyticsRepository)
    }

    @Provides
    fun provideLoadMainUserNameUseCase(getUserUseCase: GetUserUseCase): LoadMainUserNameUseCase {
        return LoadMainUserNameUseCaseImpl(getUserUseCase)
    }
}
