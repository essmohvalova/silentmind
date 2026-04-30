package com.example.coursework_app.ui.onboarding

import com.example.coursework_app.domain.model.user.CharacterType

data class OnboardingDataState(
    val userName: String = "гость",
    val userEmail: String = "",
    val selectedCharacter: CharacterType = CharacterType.CAT,
)