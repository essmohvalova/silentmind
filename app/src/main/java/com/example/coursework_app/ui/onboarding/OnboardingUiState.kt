package com.example.coursework_app.ui.onboarding

import com.example.coursework_app.domain.model.user.CharacterType

data class OnboardingUiState(
    val contentData: ContentData = ContentData(),
    val onboardingCompleted: Boolean = false,
    val currentPage: Int = 0,
) {

    data class ContentData(
        val userName: String = "гость",
        val userEmail: String = "",
        val selectedCharacter: CharacterType = CharacterType.CAT,
    )
}
