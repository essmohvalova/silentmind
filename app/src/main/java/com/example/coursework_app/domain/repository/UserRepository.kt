package com.example.coursework_app.domain.repository

import com.example.coursework_app.domain.model.user.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun saveUser(user: User)

    suspend fun getUser(id: String): User?

    fun observeUser(id: String): Flow<User?>

    suspend fun clearUserData()
}