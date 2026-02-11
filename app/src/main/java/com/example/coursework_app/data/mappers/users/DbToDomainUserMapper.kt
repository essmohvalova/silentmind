package com.example.coursework_app.data.mappers.users

import com.example.coursework_app.data.db.entity.UserEntityDb
import com.example.coursework_app.domain.model.CharacterType
import com.example.coursework_app.domain.model.user.User
import javax.inject.Inject

class DbToDomainUserMapper @Inject constructor() : (UserEntityDb) -> User{
    override fun invoke(user: UserEntityDb): User {
        return User(
            id = user.id,
            name = user.name,
            email = user.email,
            selectedCharacter = user.selectedCharacterType,
            onboardingCompleted = user.onboardingCompleted,
            createdAt = user.createdAt
        )
    }
}