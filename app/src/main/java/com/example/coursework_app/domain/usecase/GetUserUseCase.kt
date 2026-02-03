package com.example.coursework_app.domain.usecase

import com.example.coursework_app.domain.model.User
import com.example.coursework_app.domain.repository.UserRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): User? {
        return repository.getUser()
    }
}