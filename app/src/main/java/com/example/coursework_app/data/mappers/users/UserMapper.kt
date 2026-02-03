package com.example.coursework_app.data.mappers.users

import com.example.coursework_app.data.db.entity.UserEntity
import com.example.coursework_app.domain.model.CharacterType
import com.example.coursework_app.domain.model.User
import javax.inject.Inject

class UserMapper @Inject constructor() {

    fun toEntity(domain: User): UserEntity {
        return UserEntity(
            id = domain.id,
            name = domain.name,
            characterType = domain.selectedCharacter.name,
            onboardingCompleted = domain.onboardingCompleted,
            createdAt = domain.createdAt
        )
    }

    fun toDomain(entity: UserEntity?): User? {
        return entity?.let {
            User(
                id = it.id,
                name = it.name,
                selectedCharacter = CharacterType.fromName(it.characterType),
                onboardingCompleted = it.onboardingCompleted,
                createdAt = it.createdAt
            )
        }
    }

}