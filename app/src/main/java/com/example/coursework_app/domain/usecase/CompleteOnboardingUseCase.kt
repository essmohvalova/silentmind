package com.example.coursework_app.domain.usecase

import com.example.coursework_app.domain.model.user.CharacterType
import com.example.coursework_app.domain.model.user.User
import com.example.coursework_app.domain.preferences.UserPreferences
import java.util.UUID
import javax.inject.Inject

interface CompleteOnboardingUseCase {

    suspend operator fun invoke(
        userName: String,
        userEmail: String,
        selectedCharacter: CharacterType,
    ): Result<Unit>
}

class CompleteOnboardingUseCaseImpl @Inject constructor(
    private val saveUserUseCase: SaveUserUseCase,
    private val userPreferences: UserPreferences,
) : CompleteOnboardingUseCase {

    override suspend operator fun invoke(
        userName: String,
        userEmail: String,
        selectedCharacter: CharacterType,
    ): Result<Unit> = runCatching {
        val userId = userEmail.takeIf { it.isNotBlank() } ?: UUID.randomUUID().toString()
        saveUserUseCase(
            User(
                id = userId,
                name = userName,
                email = userEmail,
                selectedCharacter = selectedCharacter,
            ),
        )
        userPreferences.setUserId(userId)
        userPreferences.setOnboardingCompleted(true)
    }
}
