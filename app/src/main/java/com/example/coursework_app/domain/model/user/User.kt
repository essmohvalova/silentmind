package com.example.coursework_app.domain.model.user

import com.example.coursework_app.domain.model.CharacterType

class User(
    val id: String,

    val name: String,

    val email: String,

    val selectedCharacter: CharacterType,

    val onboardingCompleted: Boolean,

    val createdAt: Long = System.currentTimeMillis()
)