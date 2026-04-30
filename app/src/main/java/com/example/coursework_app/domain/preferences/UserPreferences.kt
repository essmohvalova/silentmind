package com.example.coursework_app.domain.preferences

interface UserPreferences {

    fun getUserId(): String?

    fun setUserId(id: String)

    fun isOnboardingCompleted(): Boolean

    fun setOnboardingCompleted(completed: Boolean)

    fun clearAll()

}