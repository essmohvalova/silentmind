package com.example.coursework_app.domain.usecase

import com.example.coursework_app.domain.model.User
import com.example.coursework_app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(): Flow<User?> {
        return repository.observeUser()
    }
}