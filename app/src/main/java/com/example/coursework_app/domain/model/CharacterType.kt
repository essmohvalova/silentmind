package com.example.coursework_app.domain.model

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