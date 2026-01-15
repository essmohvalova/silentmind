package com.example.coursework_app.data.model

import android.app.Notification

data class User(
    val id: Int,
    val user_name: String,
    val unique_name: String, // or id?
    val email: String
) {
    data class UserSettings(
        val theme: String = "light",
        val notificationsEnabled: Boolean = true,
        val language: String = "ru"
    )

    companion object {
        const val COLLECTION_NAME = "users"
    }
}