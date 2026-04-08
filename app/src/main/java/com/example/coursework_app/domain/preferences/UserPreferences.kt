package com.example.coursework_app.domain.preferences

interface UserPreferences {

    fun getUserId(): String?

    fun setUserId(id: String)

    fun clearAll()
}