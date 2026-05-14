package com.example.coursework_app.domain.usecase

import javax.inject.Inject

interface GetAnalyticsUserIdUseCase {

    suspend operator fun invoke(): String?
}

class GetAnalyticsUserIdUseCaseImpl @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
) : GetAnalyticsUserIdUseCase {

    override suspend operator fun invoke(): String? = getUserUseCase()?.id
}
