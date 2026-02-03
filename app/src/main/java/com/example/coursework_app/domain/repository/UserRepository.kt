package com.example.coursework_app.domain.repository

import com.example.coursework_app.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun saveUser(user: User)
    suspend fun getUser(): User?
    fun observeUser(): Flow<User?>
    suspend fun clearUserData()
}