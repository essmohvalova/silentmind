package com.example.coursework_app.ui.onboarding

import com.example.coursework_app.domain.model.CharacterType

sealed class OnboardingEvent {
    class OnNameChanged(val name: String) : OnboardingEvent() // class вместо data class
    class OnCharacterSelected(val character: CharacterType) : OnboardingEvent() // class вместо data class
    object OnCompleteClicked : OnboardingEvent()
    object OnSkipClicked : OnboardingEvent()
}