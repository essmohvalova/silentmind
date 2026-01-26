package com.example.coursework_app.domain.model

enum class CharacterType {
    CAT, DOG, EMOJI;

    companion object {
        fun fromName(name: String): CharacterType {
            return values().find { it.name == name } ?: CAT
        }
    }
}

data class User(
    val name: String = "",
    val selectedCharacter: CharacterType = CharacterType.CAT,
    val onboardingCompleted: Boolean = false
)