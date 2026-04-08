package com.example.coursework_app.ui.onboarding

import com.example.coursework_app.domain.model.user.CharacterType

data class OnboardingUiState(
    val name: String = "",
    val selectedCharacter: CharacterType = CharacterType.CAT,
    val onboardingCompleted: Boolean = false,
    val currentPage: Int = 0,
)
