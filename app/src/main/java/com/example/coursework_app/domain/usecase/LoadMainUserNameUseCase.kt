package com.example.coursework_app.domain.usecase

import javax.inject.Inject

interface LoadMainUserNameUseCase {

    suspend operator fun invoke(): String
}

class LoadMainUserNameUseCaseImpl @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
) : LoadMainUserNameUseCase {

    override suspend operator fun invoke(): String =
        getUserUseCase()?.name ?: DEFAULT_NAME

    private companion object {
        const val DEFAULT_NAME: String = "Гость"
    }
}
