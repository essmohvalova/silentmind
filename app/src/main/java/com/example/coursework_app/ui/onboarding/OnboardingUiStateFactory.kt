package com.example.coursework_app.ui.onboarding

import androidx.lifecycle.SavedStateHandle
import com.example.coursework_app.domain.model.user.CharacterType
import com.example.coursework_app.domain.repository.UserRepository
import javax.inject.Inject

class OnboardingUiStateFactory @Inject constructor(
    private val userRepository: UserRepository,
    private val savedStateHandle: SavedStateHandle
) {

    suspend fun create(): OnboardingUiState {
        // 1. Проверяем, есть ли уже сохранённый пользователь
        val existingUser = userRepository.getUser()

        // 2. Если пользователь существует и онбординг завершён, возвращаем завершённое состояние
        if (existingUser != null /*&& existingUser.onboardingCompleted*/) {
            return OnboardingUiState(
                dataState = OnboardingDataState(
                    userName = existingUser.name,
                    userEmail = existingUser.email,
                    selectedCharacter = existingUser.selectedCharacter
                ),
                onboardingCompleted = true,
                currentPage = 0
            )
        }

        // 3. Если есть сохранённые данные из предыдущей сессии
        val savedName = savedStateHandle.get<String>("userName") ?: ""
        val savedEmail = savedStateHandle.get<String>("userEmail") ?: ""
        val savedCharacter = savedStateHandle.get<String>("selectedCharacter")?.let {
            try {
                CharacterType.valueOf(it)
            } catch (e: IllegalArgumentException) {
                CharacterType.CAT
            }
        } ?: CharacterType.CAT
        val savedPage = savedStateHandle.get<Int>("currentPage") ?: 0

        // 4. Если есть незавершённый онбординг, продолжаем с того же места
        if (savedPage > 0 || savedName.isNotBlank() || savedEmail.isNotBlank()) {
            return OnboardingUiState(
                dataState = OnboardingDataState(
                    userName = savedName,
                    userEmail = savedEmail,
                    selectedCharacter = savedCharacter
                ),
                onboardingCompleted = false,
                currentPage = savedPage
            )
        }

        // 5. Иначе возвращаем начальное состояние
        return OnboardingUiState(
            dataState = OnboardingDataState(),
            onboardingCompleted = false,
            currentPage = 0
        )
    }
}