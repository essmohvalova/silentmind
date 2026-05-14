package com.example.coursework_app.ui.onboarding

import com.example.coursework_app.domain.model.user.CharacterType
import com.example.coursework_app.domain.model.user.User
import javax.inject.Inject

class OnboardingUiStateFactory @Inject constructor() {

    fun create(
        existingUser: User?,
        savedName: String,
        savedEmail: String,
        savedCharacter: CharacterType,
        savedPage: Int,
    ): OnboardingUiState {
        if (existingUser != null) {
            return OnboardingUiState(
                contentData = OnboardingUiState.ContentData(
                    userName = existingUser.name,
                    userEmail = existingUser.email,
                    selectedCharacter = existingUser.selectedCharacter,
                ),
                onboardingCompleted = true,
                currentPage = 0,
            )
        }

        if (savedPage > 0 || savedName.isNotBlank() || savedEmail.isNotBlank()) {
            return OnboardingUiState(
                contentData = OnboardingUiState.ContentData(
                    userName = savedName,
                    userEmail = savedEmail,
                    selectedCharacter = savedCharacter,
                ),
                onboardingCompleted = false,
                currentPage = savedPage,
            )
        }

        return OnboardingUiState(
            contentData = OnboardingUiState.ContentData(),
            onboardingCompleted = false,
            currentPage = 0,
        )
    }
}