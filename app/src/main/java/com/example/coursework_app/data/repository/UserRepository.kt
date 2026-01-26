package com.example.coursework_app.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.coursework_app.domain.model.CharacterType
import com.example.coursework_app.domain.model.User

class UserRepository(private val context: Context) {
    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }

    suspend fun saveUser(user: User) {
        // TODO: Сделать асинхронно
        prefs.edit()
            .putString("user_name", user.name)
            .putString("character_type", user.selectedCharacter.name)
            .putBoolean("onboarding_completed", true)
            .apply()
    }

    fun getUser(): User {
        return User(
            name = prefs.getString("user_name", "") ?: "",
            selectedCharacter = CharacterType.valueOf(
                prefs.getString("character_type", CharacterType.CAT.name) ?: CharacterType.CAT.name
            ),
            onboardingCompleted = prefs.getBoolean("onboarding_completed", false)
        )
    }
}