package com.example.coursework_app.data.mappers.users

import com.example.coursework_app.data.db.entity.UserEntityDb
import com.example.coursework_app.domain.model.user.User
import javax.inject.Inject

class DomainToDbUserMapper @Inject constructor() : (User) -> UserEntityDb {

    override fun invoke(userDomain: User): UserEntityDb {
        return UserEntityDb(
            id = userDomain.id,
            name = userDomain.name,
            email = userDomain.email,
            selectedCharacterType = userDomain.selectedCharacter,
            onboardingCompleted = userDomain.onboardingCompleted,
            createdAt = userDomain.createdAt,
        )
    }
}