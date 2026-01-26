package com.example.coursework_app.ui.onboarding.navigation


sealed class OnboardingDestination(val route: String) {
    object Welcome : OnboardingDestination("welcome")
    object CharacterSelection : OnboardingDestination("character_selection")
    object Registration : OnboardingDestination("registration")
}