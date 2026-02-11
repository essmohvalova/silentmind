package com.example.coursework_app.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.coursework_app.domain.model.CharacterType

@Entity(tableName = "users")
data class UserEntityDb(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "character_type")
    val selectedCharacterType: CharacterType,

    @ColumnInfo(name = "onboarding_completed")
    val onboardingCompleted: Boolean,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
) {
    constructor(
        name: String,
        email: String,
        characterType: CharacterType,
        onboardingCompleted: Boolean,
        createdAt: Long = System.currentTimeMillis()
    ) : this (
        id = generateId(email),
        name = name,
        email = email,
        selectedCharacterType = characterType,
        onboardingCompleted = onboardingCompleted,
        createdAt = createdAt
    )
    companion object {
        fun generateId(email: String): String {
            val timestamp = System.currentTimeMillis()
            return "${email}_$timestamp"
        }
    }
}