package com.example.coursework_app.ui.welcome

data class WelcomeState(
    val isLoading: Boolean = true,
    val destination: Destination? = null
) {

    enum class Destination {
        MAIN,
        ONBOARDING;
    }
}
