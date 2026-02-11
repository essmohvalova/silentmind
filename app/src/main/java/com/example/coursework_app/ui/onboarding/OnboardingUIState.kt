package com.example.coursework_app.ui.onboarding

import com.example.coursework_app.domain.model.CharacterType

data class OnboardingUiState(
    val name: String = "",
    val selectedCharacter: CharacterType? = null,
    val isCompleteButtonEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
) {
    fun isFormValid(): Boolean = name.isNotBlank() && selectedCharacter != null
}