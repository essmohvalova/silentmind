package com.example.coursework_app.domain.usecase

import com.example.coursework_app.domain.model.user.User
import com.example.coursework_app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface SaveUserUseCase {
    suspend operator fun invoke(user: User)
}
class SaveUserUseCaseImpl @Inject constructor(
    private val repository: UserRepository
) : SaveUserUseCase {

    override suspend operator fun invoke(user: User) {
        repository.saveUser(user)
    }
}