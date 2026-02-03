package com.example.coursework_app.domain.model


data class User(
    val id: Int,
    val name: String,
    val selectedCharacter: CharacterType,
    val onboardingCompleted: Boolean,
    val createdAt: Long = System.currentTimeMillis()
)

enum class CharacterType {
    CAT, DOG, EMOJI;

    companion object {
        fun fromName(name: String): CharacterType {
            return try {
                valueOf(name)
            } catch (e: IllegalArgumentException) {
                CAT
            }
        }
    }
}