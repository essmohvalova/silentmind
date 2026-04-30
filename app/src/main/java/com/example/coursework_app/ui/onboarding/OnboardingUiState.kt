package com.example.coursework_app.ui.onboarding

data class OnboardingUiState(
    val dataState: OnboardingDataState = OnboardingDataState(),
    val onboardingCompleted: Boolean = false,
    val currentPage: Int = 0,
)
