package com.example.coursework_app.domain.usecase

import com.example.coursework_app.domain.preferences.UserPreferences
import javax.inject.Inject

interface IsUserAuthorizedUseCase {

    suspend operator fun invoke(): Boolean
}

class IsUserAuthorizedUseCaseImpl @Inject constructor(
    private val userPreferences: UserPreferences,
) : IsUserAuthorizedUseCase{

    override suspend operator fun invoke(): Boolean {
        return userPreferences.getUserId() != null
    }
}
