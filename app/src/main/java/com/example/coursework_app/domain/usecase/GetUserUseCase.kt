package com.example.coursework_app.domain.usecase

import com.example.coursework_app.domain.model.user.User
import com.example.coursework_app.domain.repository.UserRepository
import javax.inject.Inject

interface GetUserUseCase {

    suspend operator fun invoke(): User?
}

class GetUserUseCaseImpl @Inject constructor(
    private val repository: UserRepository
) : GetUserUseCase{

    override suspend operator fun invoke(): User? {
        return repository.getUser()
    }
}
