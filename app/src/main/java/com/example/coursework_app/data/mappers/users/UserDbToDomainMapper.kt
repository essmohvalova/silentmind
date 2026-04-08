package com.example.coursework_app.data.mappers.users

import com.example.coursework_app.data.db.entity.user.CharacterTypeDb
import com.example.coursework_app.data.db.entity.user.UserEntityDb
import com.example.coursework_app.domain.model.user.CharacterType
import com.example.coursework_app.domain.model.user.User
import javax.inject.Inject

class UserDbToDomainMapper @Inject constructor() : (UserEntityDb) -> User{

    override fun invoke(user: UserEntityDb): User {
        return User(
            id = user.id,
            name = user.name,
            email = user.email,
            selectedCharacter = user.characterType.toDomain(),
        )
    }

    private fun CharacterTypeDb.toDomain(): CharacterType {
        return when(this) {
            CharacterTypeDb.CAT -> CharacterType.CAT
            CharacterTypeDb.DOG -> CharacterType.DOG
            CharacterTypeDb.EMOJI -> CharacterType.EMOJI
        }
    }
}
