package com.example.coursework_app.domain.usecase

import com.example.coursework_app.domain.model.user.User
import com.example.coursework_app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ObserveUserUseCase {
    suspend operator fun invoke(id: String) : Flow<User?>
}

class ObserveUserUseCaseImpl @Inject constructor(
    private val repository: UserRepository
) : ObserveUserUseCase {

    override suspend operator fun invoke(id: String): Flow<User?> {
        return repository.observeUser(id)
    }
}