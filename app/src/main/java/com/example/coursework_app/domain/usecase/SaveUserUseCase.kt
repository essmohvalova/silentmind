package com.example.coursework_app.domain.usecase

import com.example.coursework_app.domain.model.User
import com.example.coursework_app.domain.repository.UserRepository
import javax.inject.Inject

class SaveUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(user: User) {
        repository.saveUser(user)
    }
}